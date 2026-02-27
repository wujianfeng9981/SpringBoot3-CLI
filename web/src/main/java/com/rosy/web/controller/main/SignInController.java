package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.signin.SignInQueryRequest;
import com.rosy.main.domain.dto.signin.SignInRequest;
import com.rosy.main.domain.entity.Reservation;
import com.rosy.main.domain.entity.SignIn;
import com.rosy.main.domain.enums.ReservationStatus;
import com.rosy.main.domain.enums.SignInStatus;
import com.rosy.main.domain.vo.SignInVO;
import com.rosy.main.service.IReservationService;
import com.rosy.main.service.ISignInService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/signin")
public class SignInController {

    @Resource
    private ISignInService signInService;

    @Resource
    private IReservationService reservationService;

    @PostMapping("/do")
    @ValidateRequest
    public ApiResponse doSignIn(@RequestBody SignInRequest signInRequest) {
        Reservation reservation = reservationService.getById(signInRequest.getReservationId());
        ThrowUtils.throwIf(reservation == null, ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        
        if (!reservation.getStatus().equals(ReservationStatus.APPROVED.getCode())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该预约未通过审批，无法签到");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime signInDeadline = reservation.getEndTime().plusMinutes(30);
        ThrowUtils.throwIf(now.isAfter(signInDeadline), ErrorCode.OPERATION_ERROR, "已超过签到截止时间");

        SignIn signIn = new SignIn();
        signIn.setReservationId(signInRequest.getReservationId());
        signIn.setUserId(signInRequest.getUserId());
        signIn.setRemark(signInRequest.getRemark());
        signIn.setSignInTime(now);

        LocalDateTime meetingStart = reservation.getStartTime();
        if (now.isBefore(meetingStart) || now.isEqual(meetingStart)) {
            signIn.setSignInStatus(SignInStatus.SIGNED.getCode());
        } else if (now.isBefore(meetingStart.plusMinutes(15))) {
            signIn.setSignInStatus(SignInStatus.SIGNED.getCode());
        } else {
            signIn.setSignInStatus(SignInStatus.LATE.getCode());
        }

        boolean result = signInService.save(signIn);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(signIn.getId());
    }

    @GetMapping("/get")
    public ApiResponse getSignInById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SignIn signIn = signInService.getById(id);
        ThrowUtils.throwIf(signIn == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(signIn);
    }

    @GetMapping("/get/vo")
    public ApiResponse getSignInVOById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SignIn signIn = signInService.getById(id);
        ThrowUtils.throwIf(signIn == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(signInService.getSignInVO(signIn));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listSignInByPage(@RequestBody SignInQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<SignIn> signInPage = signInService.page(
                new Page<>(current, size),
                signInService.getQueryWrapper(queryRequest)
        );
        return ApiResponse.success(signInPage);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listSignInVOByPage(@RequestBody SignInQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<SignIn> signInPage = signInService.page(
                new Page<>(current, size),
                signInService.getQueryWrapper(queryRequest)
        );
        Page<SignInVO> signInVOPage = PageUtils.convert(signInPage, signInService::getSignInVO);
        return ApiResponse.success(signInVOPage);
    }

    @GetMapping("/statistics")
    public ApiResponse getSignInStatistics(Long reservationId) {
        if (reservationId == null || reservationId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int signedCount = signInService.countSignedByReservationId(reservationId);
        int totalCount = signInService.countTotalByReservationId(reservationId);
        return ApiResponse.success(new Object() {
            public final int signed = signedCount;
            public final int total = totalCount;
            public final double rate = totalCount > 0 ? (double) signedCount / totalCount * 100 : 0;
        });
    }
}
