package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.BookingStatusEnum;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.booking.BookingAddRequest;
import com.rosy.main.domain.dto.booking.BookingApprovalRequest;
import com.rosy.main.domain.dto.booking.BookingQueryRequest;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.entity.BookingApproval;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.BookingVO;
import com.rosy.main.mapper.BookingMapper;
import com.rosy.main.service.IBookingApprovalService;
import com.rosy.main.service.IBookingService;
import com.rosy.main.service.IMeetingRoomService;
import com.rosy.main.service.INotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl extends ServiceImpl<BookingMapper, Booking> implements IBookingService {

    private final IMeetingRoomService meetingRoomService;
    private final IBookingApprovalService bookingApprovalService;
    private final INotificationService notificationService;

    public BookingServiceImpl(IMeetingRoomService meetingRoomService,
                              @Lazy IBookingApprovalService bookingApprovalService,
                              @Lazy INotificationService notificationService) {
        this.meetingRoomService = meetingRoomService;
        this.bookingApprovalService = bookingApprovalService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addBooking(BookingAddRequest request) {
        validateBookingRequest(request);

        QueryWrapper<Booking> conflictWrapper = new QueryWrapper<>();
        conflictWrapper.eq("room_id", request.getRoomId());
        conflictWrapper.in("status", BookingStatusEnum.PENDING.getCode(), BookingStatusEnum.APPROVED.getCode());
        conflictWrapper.lt("start_time", request.getEndTime());
        conflictWrapper.gt("end_time", request.getStartTime());
        conflictWrapper.eq("is_deleted", 0);
        Booking conflict = this.getOne(conflictWrapper);
        if (conflict != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该时间段会议室已被预约");
        }

        Booking booking = new Booking();
        booking.setRoomId(request.getRoomId());
        booking.setUserId(request.getUserId());
        booking.setUserName(request.getUserName());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setSubject(request.getSubject());
        booking.setStatus((byte) BookingStatusEnum.PENDING.getCode());
        booking.setCreatorId(request.getUserId());

        this.save(booking);
        log.info("创建预约成功, bookingId={}, roomId={}, userId={}",
                booking.getId(), request.getRoomId(), request.getUserId());
        return booking.getId();
    }

    private void validateBookingRequest(BookingAddRequest request) {
        if (request.getRoomId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择会议室");
        }
        MeetingRoom room = meetingRoomService.getById(request.getRoomId());
        if (room == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
        }
        if (room.getStatus() == null || room.getStatus() == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该会议室暂不可预约");
        }

        LocalDateTime now = LocalDateTime.now();
        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择预约时间段");
        }
        if (request.getStartTime().isBefore(now)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "预约开始时间不能早于当前时间");
        }
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始时间不能晚于结束时间");
        }
        if (request.getStartTime().equals(request.getEndTime())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始时间和结束时间不能相同");
        }
        Duration duration = Duration.between(request.getStartTime(), request.getEndTime());
        if (duration.toMinutes() < 30) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最少预约30分钟");
        }
        if (duration.toHours() > 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "单次预约不能超过8小时");
        }

        if (StringUtils.isBlank(request.getSubject())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请填写会议事由");
        }
        if (request.getSubject().length() > 500) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "事由不能超过500个字符");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelBooking(Long id, Long userId) {
        Booking booking = this.getById(id);
        if (booking == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        }
        if (!booking.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "没有权限取消该预约");
        }
        if (booking.getStatus() == BookingStatusEnum.CANCELLED.getCode() ||
            booking.getStatus() == BookingStatusEnum.REJECTED.getCode()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该预约已取消或已驳回");
        }
        if (booking.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "会议已开始，无法取消");
        }

        booking.setStatus((byte) BookingStatusEnum.CANCELLED.getCode());
        boolean result = this.updateById(booking);
        log.info("取消预约成功, bookingId={}, userId={}", id, userId);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approveBooking(BookingApprovalRequest request) {
        return processApproval(request, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean rejectBooking(BookingApprovalRequest request) {
        return processApproval(request, false);
    }

    private Boolean processApproval(BookingApprovalRequest request, boolean isApproved) {
        Booking booking = this.getById(request.getBookingId());
        if (booking == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        }
        if (booking.getStatus() != BookingStatusEnum.PENDING.getCode()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该预约已处理过");
        }

        if (isApproved) {
            booking.setStatus((byte) BookingStatusEnum.APPROVED.getCode());
        } else {
            booking.setStatus((byte) BookingStatusEnum.REJECTED.getCode());
            booking.setRejectReason(request.getComment());
        }

        this.updateById(booking);

        BookingApproval approval = new BookingApproval();
        approval.setBookingId(request.getBookingId());
        approval.setApproverId(request.getApproverId());
        approval.setApproverName("审批人");
        approval.setApprovalResult((byte) (isApproved ? 1 : 2));
        approval.setComment(request.getComment());
        approval.setApprovalTime(LocalDateTime.now());
        bookingApprovalService.save(approval);

        notificationService.sendApprovalNotification(booking, isApproved);
        log.info("审批预约完成, bookingId={}, approved={}, approverId={}",
                request.getBookingId(), isApproved, request.getApproverId());
        return true;
    }

    @Override
    public BookingVO getBookingById(Long id) {
        Booking booking = this.getById(id);
        return toBookingVO(booking);
    }

    @Override
    public List<BookingVO> getBookingList(BookingQueryRequest request) {
        QueryWrapper<Booking> queryWrapper = getQueryWrapper(request);
        List<Booking> list = this.list(queryWrapper);
        return list.stream()
                .map(this::toBookingVO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookingVO> getBookingPage(BookingQueryRequest request, Integer pageNum, Integer pageSize) {
        QueryWrapper<Booking> queryWrapper = getQueryWrapper(request);
        Page<Booking> page = this.page(
                new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10),
                queryWrapper
        );
        return page.convert(this::toBookingVO);
    }

    @Override
    public QueryWrapper<Booking> getQueryWrapper(BookingQueryRequest request) {
        QueryWrapper<Booking> queryWrapper = new QueryWrapper<>();

        if (request.getRoomId() != null) {
            queryWrapper.eq("room_id", request.getRoomId());
        }
        if (request.getUserId() != null) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        if (request.getStatus() != null) {
            queryWrapper.eq("status", request.getStatus());
        }
        if (request.getStartDate() != null) {
            queryWrapper.ge("start_time", request.getStartDate());
        }
        if (request.getEndDate() != null) {
            queryWrapper.le("end_time", request.getEndDate());
        }

        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    @Override
    public BookingVO toBookingVO(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingVO vo = Optional.of(booking)
                .map(b -> BeanUtil.copyProperties(b, BookingVO.class))
                .orElse(null);

        if (vo != null) {
            BookingStatusEnum statusEnum = getStatusEnum(booking.getStatus());
            if (statusEnum != null) {
                vo.setStatusText(statusEnum.getText());
            }

            MeetingRoom room = meetingRoomService.getById(booking.getRoomId());
            if (room != null) {
                vo.setRoomName(room.getName());
            }
        }
        return vo;
    }

    private BookingStatusEnum getStatusEnum(Byte status) {
        if (status == null) return null;
        for (BookingStatusEnum statusEnum : BookingStatusEnum.values()) {
            if (statusEnum.getCode() == status) {
                return statusEnum;
            }
        }
        return null;
    }
}
