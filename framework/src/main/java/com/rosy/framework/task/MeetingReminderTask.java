package com.rosy.framework.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.entity.Reservation;
import com.rosy.main.domain.enums.ReservationStatus;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.IReservationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class MeetingReminderTask {

    @Resource
    private IReservationService reservationService;

    @Resource
    private INotificationService notificationService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void sendMeetingReminders() {
        log.info("开始执行会议提醒任务...");
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusMinutes(30);

        LambdaQueryWrapper<Reservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reservation::getStatus, ReservationStatus.APPROVED.getCode());
        wrapper.gt(Reservation::getStartTime, now);
        wrapper.le(Reservation::getStartTime, reminderTime);

        List<Reservation> reservations = reservationService.list(wrapper);

        for (Reservation reservation : reservations) {
            boolean alreadyNotified = checkIfAlreadyNotified(reservation.getId(), reservation.getApplicantId());
            if (!alreadyNotified) {
                notificationService.sendMeetingReminder(
                        reservation.getApplicantId(),
                        reservation.getId(),
                        reservation.getTitle(),
                        reservation.getStartTime()
                );
                log.info("已发送会议提醒：预约ID={}, 主题={}", reservation.getId(), reservation.getTitle());
            }
        }

        log.info("会议提醒任务执行完成，共发送{}条提醒", reservations.size());
    }

    private boolean checkIfAlreadyNotified(Long reservationId, Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getReservationId, reservationId);
        wrapper.eq(Notification::getUserId, userId);
        wrapper.eq(Notification::getType, (byte) 1);
        return notificationService.count(wrapper) > 0;
    }
}
