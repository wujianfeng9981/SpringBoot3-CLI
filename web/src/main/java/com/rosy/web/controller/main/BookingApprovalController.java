package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalAddRequest;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalQueryRequest;
import com.rosy.main.domain.entity.BookingApproval;
import com.rosy.main.domain.vo.BookingApprovalVO;
import com.rosy.main.service.IBookingApprovalService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking-approval")
public class BookingApprovalController {

    @Resource
    private IBookingApprovalService bookingApprovalService;

    @PostMapping("/approve")
    @ValidateRequest
    public ApiResponse approve(@RequestBody BookingApprovalAddRequest request) {
        Long approverId = 1L;
        bookingApprovalService.approve(request, approverId);
        return ApiResponse.success();
    }

    @GetMapping("/get")
    public ApiResponse getApprovalById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        BookingApproval approval = bookingApprovalService.getById(id);
        ThrowUtils.throwIf(approval == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(approval);
    }

    @GetMapping("/get/vo")
    public ApiResponse getApprovalVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        BookingApproval approval = bookingApprovalService.getById(id);
        ThrowUtils.throwIf(approval == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(bookingApprovalService.getApprovalVO(approval));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listApprovalByPage(@RequestBody BookingApprovalQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<BookingApproval> page = bookingApprovalService.page(
                new Page<>(current, size),
                bookingApprovalService.getQueryWrapper(queryRequest)
        );
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listApprovalVOByPage(@RequestBody BookingApprovalQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<BookingApproval> page = bookingApprovalService.page(
                new Page<>(current, size),
                bookingApprovalService.getQueryWrapper(queryRequest)
        );
        Page<BookingApprovalVO> voPage = bookingApprovalService.getApprovalVOPage(page);
        return ApiResponse.success(voPage);
    }
}
