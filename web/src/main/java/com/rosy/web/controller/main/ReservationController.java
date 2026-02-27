package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.reservation.ReservationAddRequest;
import com.rosy.main.domain.dto.reservation.ReservationApprovalRequest;
import com.rosy.main.domain.dto.reservation.ReservationCancelRequest;
import com.rosy.main.domain.dto.reservation.ReservationQueryRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.entity.Reservation;
import com.rosy.main.domain.enums.ReservationStatus;
import com.rosy.main.domain.enums.RoomStatus;
import com.rosy.main.domain.vo.ReservationVO;
import com.rosy.main.service.IMeetingRoomService;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.IReservationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Resource
    private IReservationService reservationService;

    @Resource
    private IMeetingRoomService meetingRoomService;

    @Resource
    private INotificationService notificationService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addReservation(@RequestBody ReservationAddRequest addRequest) {
        if (addRequest.getStartTime().isAfter(addRequest.getEndTime())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始时间不能晚于结束时间");
        }
        if (addRequest.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始时间不能早于当前时间");
        }

        MeetingRoom room = meetingRoomService.getById(addRequest.getRoomId());
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
        ThrowUtils.throwIf(room.getStatus().equals(RoomStatus.DISABLED.getCode()), 
                ErrorCode.OPERATION_ERROR, "会议室已停用");
        ThrowUtils.throwIf(room.getStatus().equals(RoomStatus.MAINTENANCE.getCode()), 
                ErrorCode.OPERATION_ERROR, "会议室维护中");

        if (addRequest.getAttendeeCount() > room.getCapacity()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参会人数超过会议室容量");
        }

        boolean hasConflict = reservationService.hasConflict(
                addRequest.getRoomId(),
                addRequest.getStartTime(),
                addRequest.getEndTime()
        );
        ThrowUtils.throwIf(hasConflict, ErrorCode.OPERATION_ERROR, "该时间段会议室已被预约");

        Reservation reservation = BeanUtil.copyProperties(addRequest, Reservation.class);
        reservation.setStatus(ReservationStatus.PENDING.getCode());
        boolean result = reservationService.save(reservation);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(reservation.getId());
    }

    @PostMapping("/cancel")
    @ValidateRequest
    public ApiResponse cancelReservation(@RequestBody ReservationCancelRequest cancelRequest) {
        Reservation reservation = reservationService.getById(cancelRequest.getReservationId());
        ThrowUtils.throwIf(reservation == null, ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        
        if (!reservation.getStatus().equals(ReservationStatus.PENDING.getCode()) 
                && !reservation.getStatus().equals(ReservationStatus.APPROVED.getCode())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前状态无法取消");
        }

        reservation.setStatus(ReservationStatus.CANCELLED.getCode());
        boolean result = reservationService.updateById(reservation);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

        notificationService.sendApprovalNotification(
                reservation.getApplicantId(),
                reservation.getId(),
                false,
                "预约已取消"
        );
        
        return ApiResponse.success(true);
    }

    @PostMapping("/approve")
    @ValidateRequest
    public ApiResponse approveReservation(@RequestBody ReservationApprovalRequest approvalRequest) {
        Reservation reservation = reservationService.getById(approvalRequest.getReservationId());
        ThrowUtils.throwIf(reservation == null, ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        
        if (!reservation.getStatus().equals(ReservationStatus.PENDING.getCode())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该预约不在待审批状态");
        }

        boolean approved = approvalRequest.getApprovalStatus().equals((byte) 1);
        
        if (approved) {
            boolean hasConflict = reservationService.hasConflictExcludeId(
                    reservation.getRoomId(),
                    reservation.getStartTime(),
                    reservation.getEndTime(),
                    reservation.getId()
            );
            ThrowUtils.throwIf(hasConflict, ErrorCode.OPERATION_ERROR, "该时间段会议室已被其他预约占用");
            
            reservation.setStatus(ReservationStatus.APPROVED.getCode());
        } else {
            reservation.setStatus(ReservationStatus.REJECTED.getCode());
            reservation.setRejectReason(approvalRequest.getRejectReason());
        }

        boolean result = reservationService.updateById(reservation);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

        notificationService.sendApprovalNotification(
                reservation.getApplicantId(),
                reservation.getId(),
                approved,
                approvalRequest.getRejectReason()
        );

        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    public ApiResponse getReservationById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Reservation reservation = reservationService.getById(id);
        ThrowUtils.throwIf(reservation == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(reservation);
    }

    @GetMapping("/get/vo")
    public ApiResponse getReservationVOById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Reservation reservation = reservationService.getById(id);
        ThrowUtils.throwIf(reservation == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(reservationService.getReservationVO(reservation));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listReservationByPage(@RequestBody ReservationQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<Reservation> reservationPage = reservationService.page(
                new Page<>(current, size),
                reservationService.getQueryWrapper(queryRequest)
        );
        return ApiResponse.success(reservationPage);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listReservationVOByPage(@RequestBody ReservationQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Reservation> reservationPage = reservationService.page(
                new Page<>(current, size),
                reservationService.getQueryWrapper(queryRequest)
        );
        Page<ReservationVO> reservationVOPage = PageUtils.convert(reservationPage, reservationService::getReservationVO);
        return ApiResponse.success(reservationVOPage);
    }
}
