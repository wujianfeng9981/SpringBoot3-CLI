package com.rosy.main.modules.meeting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.modules.meeting.dto.BookingDTO;
import com.rosy.main.modules.meeting.dto.BookingRequest;
import com.rosy.main.modules.meeting.entity.Booking;
import com.rosy.main.modules.meeting.entity.MeetingRoom;
import com.rosy.main.modules.meeting.enums.BookingStatus;
import com.rosy.main.modules.meeting.mapper.BookingMapper;
import com.rosy.main.modules.meeting.mapper.MeetingRoomMapper;
import com.rosy.main.modules.meeting.service.IBookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预约服务实现类
 * 实现预约相关的业务逻辑
 */
@Slf4j
@Service
public class BookingServiceImpl extends ServiceImpl<BookingMapper, Booking> implements IBookingService {

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    @Override
    public BookingDTO createBooking(BookingRequest request) {
        // 参数校验
        validateBookingRequest(request);

        // 查询会议室是否存在
        MeetingRoom room = meetingRoomMapper.selectById(request.getRoomId());
        if (room == null) {
            throw new RuntimeException("会议室不存在");
        }

        // 检查会议室是否启用
        if (!room.getIsActive()) {
            throw new RuntimeException("会议室已停用，无法预约");
        }

        // 检查容量是否足够
        if (request.getAttendeesCount() != null && room.getCapacity() < request.getAttendeesCount()) {
            throw new RuntimeException("会议室容量不足，无法容纳" + request.getAttendeesCount() + "人");
        }

        // 检查时间冲突
        List<Booking> conflicts = baseMapper.selectConflictingBookings(
                request.getRoomId(), request.getStartTime(), request.getEndTime());
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("该时间段已被预约，请选择其他时间");
        }

        // 创建预约实体
        Booking booking = new Booking();
        BeanUtils.copyProperties(request, booking);
        booking.setStatus(BookingStatus.PENDING);

        // 保存到数据库
        baseMapper.insert(booking);
        log.info("创建预约成功: roomId={}, user={}, startTime={}",
                request.getRoomId(), request.getUserName(), request.getStartTime());

        // 返回DTO
        return convertToDTO(booking, room.getName());
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        Booking booking = baseMapper.selectById(id);
        if (booking == null) {
            throw new RuntimeException("预约记录不存在");
        }

        // 查询会议室名称
        MeetingRoom room = meetingRoomMapper.selectById(booking.getRoomId());
        String roomName = room != null ? room.getName() : "";

        return convertToDTO(booking, roomName);
    }

    @Override
    public List<BookingDTO> getUserBookings(Long userId) {
        List<Booking> bookings = baseMapper.selectByUserId(userId);
        return bookings.stream()
                .map(booking -> {
                    MeetingRoom room = meetingRoomMapper.selectById(booking.getRoomId());
                    String roomName = room != null ? room.getName() : "";
                    return convertToDTO(booking, roomName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getRoomBookings(Long roomId, BookingStatus status) {
        List<Booking> bookings = baseMapper.selectByRoomIdAndStatus(roomId, status.name());
        MeetingRoom room = meetingRoomMapper.selectById(roomId);
        String roomName = room != null ? room.getName() : "";

        return bookings.stream()
                .map(booking -> convertToDTO(booking, roomName))
                .collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(Long id, Long userId) {
        Booking booking = baseMapper.selectById(id);
        if (booking == null) {
            throw new RuntimeException("预约记录不存在");
        }

        // 权限校验
        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("无权取消他人的预约");
        }

        // 状态校验
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("预约已取消");
        }
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("会议已完成，无法取消");
        }
        if (booking.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("会议已开始，无法取消");
        }

        // 更新状态为已取消
        booking.setStatus(BookingStatus.CANCELLED);
        baseMapper.updateById(booking);
        log.info("取消预约成功: id={}", id);
    }

    @Override
    public void approveBooking(Long id, Boolean approved, String comment) {
        Booking booking = baseMapper.selectById(id);
        if (booking == null) {
            throw new RuntimeException("预约记录不存在");
        }

        // 状态校验
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("该预约已处理，当前状态：" + booking.getStatus().getDescription());
        }

        // 更新状态
        if (Boolean.TRUE.equals(approved)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            booking.setRejectionReason(comment);
        }

        baseMapper.updateById(booking);
        log.info("审批预约成功: id={}, approved={}", id, approved);
    }

    @Override
    public List<BookingDTO> getUpcomingMeetings(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusHours(24);

        List<Booking> bookings = baseMapper.selectUpcomingMeetings(now, next24Hours);
        return bookings.stream()
                .filter(booking -> booking.getUserId().equals(userId))
                .map(booking -> {
                    MeetingRoom room = meetingRoomMapper.selectById(booking.getRoomId());
                    String roomName = room != null ? room.getName() : "";
                    return convertToDTO(booking, roomName);
                })
                .collect(Collectors.toList());
    }

    /**
     * 校验预约请求参数
     * @param request 预约请求
     */
    private void validateBookingRequest(BookingRequest request) {
        // 时间顺序校验
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("开始时间不能晚于结束时间");
        }

        // 时间相同校验
        if (request.getStartTime().isEqual(request.getEndTime())) {
            throw new RuntimeException("开始时间和结束时间不能相同");
        }

        // 过去时间校验
        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("开始时间不能是过去时间");
        }

        // 计算会议时长
        long durationMinutes = ChronoUnit.MINUTES.between(request.getStartTime(), request.getEndTime());

        // 最短时长校验
        if (durationMinutes < 15) {
            throw new RuntimeException("会议时长至少15分钟");
        }

        // 最长时长校验
        if (durationMinutes > 480) {
            throw new RuntimeException("会议时长不能超过8小时");
        }
    }

    /**
     * 将实体转换为DTO
     * @param booking 预约实体
     * @param roomName 会议室名称
     * @return 预约DTO
     */
    private BookingDTO convertToDTO(Booking booking, String roomName) {
        BookingDTO dto = new BookingDTO();
        BeanUtils.copyProperties(booking, dto);
        dto.setRoomName(roomName);
        return dto;
    }
}
