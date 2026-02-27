package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.main.domain.dto.statistics.RoomUsageQueryRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.entity.Reservation;
import com.rosy.main.domain.entity.SignIn;
import com.rosy.main.domain.enums.ReservationStatus;
import com.rosy.main.domain.enums.SignInStatus;
import com.rosy.main.domain.vo.RoomUsageStatisticsVO;
import com.rosy.main.service.IMeetingRoomService;
import com.rosy.main.service.IReservationService;
import com.rosy.main.service.ISignInService;
import com.rosy.main.service.IStatisticsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Resource
    private IMeetingRoomService meetingRoomService;

    @Resource
    private IReservationService reservationService;

    @Resource
    private ISignInService signInService;

    @Override
    public RoomUsageStatisticsVO getRoomUsageStatistics(Long roomId, RoomUsageQueryRequest queryRequest) {
        MeetingRoom room = meetingRoomService.getById(roomId);
        if (room == null) {
            return null;
        }

        RoomUsageStatisticsVO vo = new RoomUsageStatisticsVO();
        vo.setRoomId(roomId);
        vo.setRoomName(room.getName());

        LambdaQueryWrapper<Reservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reservation::getRoomId, roomId);
        
        if (queryRequest != null) {
            if (queryRequest.getStartTime() != null) {
                wrapper.ge(Reservation::getStartTime, queryRequest.getStartTime());
            }
            if (queryRequest.getEndTime() != null) {
                wrapper.le(Reservation::getEndTime, queryRequest.getEndTime());
            }
        }

        List<Reservation> reservations = reservationService.list(wrapper);

        long totalReservations = reservations.size();
        long completedReservations = reservations.stream()
                .filter(r -> r.getStatus().equals(ReservationStatus.COMPLETED.getCode()))
                .count();
        long cancelledReservations = reservations.stream()
                .filter(r -> r.getStatus().equals(ReservationStatus.CANCELLED.getCode()))
                .count();

        vo.setTotalReservations(totalReservations);
        vo.setCompletedReservations(completedReservations);
        vo.setCancelledReservations(cancelledReservations);

        if (totalReservations > 0) {
            vo.setUsageRate((double) completedReservations / totalReservations * 100);
        } else {
            vo.setUsageRate(0.0);
        }

        double totalDuration = reservations.stream()
                .filter(r -> r.getStatus().equals(ReservationStatus.COMPLETED.getCode()))
                .mapToDouble(r -> Duration.between(r.getStartTime(), r.getEndTime()).toMinutes())
                .average()
                .orElse(0.0);
        vo.setAverageDuration(totalDuration);

        long totalSignInCount = 0;
        long totalAbsentCount = 0;
        for (Reservation reservation : reservations) {
            if (reservation.getStatus().equals(ReservationStatus.COMPLETED.getCode())) {
                LambdaQueryWrapper<SignIn> signInWrapper = new LambdaQueryWrapper<>();
                signInWrapper.eq(SignIn::getReservationId, reservation.getId());
                List<SignIn> signIns = signInService.list(signInWrapper);
                
                totalSignInCount += signIns.stream()
                        .filter(s -> s.getSignInStatus().equals(SignInStatus.SIGNED.getCode()) 
                                || s.getSignInStatus().equals(SignInStatus.LATE.getCode()))
                        .count();
                totalAbsentCount += signIns.stream()
                        .filter(s -> s.getSignInStatus().equals(SignInStatus.ABSENT.getCode()))
                        .count();
            }
        }
        vo.setTotalSignInCount(totalSignInCount);
        vo.setTotalAbsentCount(totalAbsentCount);

        return vo;
    }

    @Override
    public List<RoomUsageStatisticsVO> getAllRoomsUsageStatistics(RoomUsageQueryRequest queryRequest) {
        List<MeetingRoom> rooms = meetingRoomService.list();
        List<RoomUsageStatisticsVO> result = new ArrayList<>();
        
        for (MeetingRoom room : rooms) {
            RoomUsageStatisticsVO vo = getRoomUsageStatistics(room.getId(), queryRequest);
            if (vo != null) {
                result.add(vo);
            }
        }
        
        return result;
    }
}
