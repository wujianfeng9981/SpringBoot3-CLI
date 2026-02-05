package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalAddRequest;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalQueryRequest;
import com.rosy.main.domain.entity.BookingApproval;
import com.rosy.main.domain.vo.BookingApprovalVO;
import com.rosy.main.service.IBookingApprovalService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 预约审批表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/booking-approval")
public class BookingApprovalController {
    @Resource
    IBookingApprovalService bookingApprovalService;

    /**
     * 审批预约
     */
    @PostMapping("/approve")
    @ValidateRequest
    public ApiResponse approve(@RequestBody BookingApprovalAddRequest request) {
        // 暂时使用固定值，实际应从登录用户获取
        Long approverId = 1L;
        boolean result = bookingApprovalService.approve(request, approverId);
        return ApiResponse.success(result);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    public ApiResponse getApprovalById(@RequestParam("id") Long id) {
        BookingApproval approval = bookingApprovalService.getById(id);
        ThrowUtils.throwIf(approval == null, ErrorCode.NOT_FOUND_ERROR);
        BookingApprovalVO approvalVO = bookingApprovalService.getApprovalVO(approval);
        return ApiResponse.success(approvalVO);
    }

    /**
     * 分页获取列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listApprovalByPage(@RequestBody BookingApprovalQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<BookingApproval> approvalPage = bookingApprovalService.page(
                new Page<>(current, size),
                bookingApprovalService.getQueryWrapper(request)
        );
        Page<BookingApprovalVO> approvalVOPage = bookingApprovalService.getApprovalVOPage(approvalPage);
        return ApiResponse.success(approvalVOPage);
    }
}
