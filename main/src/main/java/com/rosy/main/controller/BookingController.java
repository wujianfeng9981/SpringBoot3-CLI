package com.rosy.main.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.booking.BookingAddRequest;
import com.rosy.main.domain.dto.booking.BookingApprovalRequest;
import com.rosy.main.domain.dto.booking.BookingQueryRequest;
import com.rosy.main.domain.vo.BookingVO;
import com.rosy.main.service.IBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "会议室预约管理", description = "会议室预约的创建、取消、审批、查询等接口")
@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final IBookingService bookingService;

    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "创建预约", description = "创建新的会议室预约，会自动校验时间段冲突")
    @PostMapping
    public ApiResponse<Long> addBooking(@RequestBody BookingAddRequest request) {
        Long bookingId = bookingService.addBooking(request);
        return ApiResponse.success(bookingId);
    }

    @Operation(summary = "取消预约", description = "取消本人的预约，只有预约者本人可以取消")
    @PutMapping("/cancel/{id}")
    public ApiResponse<Boolean> cancelBooking(
            @Parameter(description = "预约ID") @PathVariable Long id,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        Boolean result = bookingService.cancelBooking(id, userId);
        return ApiResponse.success(result);
    }

    @Operation(summary = "通过预约", description = "审批通过预约申请")
    @PutMapping("/approve")
    public ApiResponse<Boolean> approveBooking(@RequestBody BookingApprovalRequest request) {
        Boolean result = bookingService.approveBooking(request);
        return ApiResponse.success(result);
    }

    @Operation(summary = "驳回预约", description = "驳回预约申请，需要填写驳回原因")
    @PutMapping("/reject")
    public ApiResponse<Boolean> rejectBooking(@RequestBody BookingApprovalRequest request) {
        Boolean result = bookingService.rejectBooking(request);
        return ApiResponse.success(result);
    }

    @Operation(summary = "获取预约详情", description = "根据预约ID获取预约详细信息")
    @GetMapping("/{id}")
    public ApiResponse<BookingVO> getBookingById(
            @Parameter(description = "预约ID") @PathVariable Long id) {
        BookingVO booking = bookingService.getBookingById(id);
        return ApiResponse.success(booking);
    }

    @Operation(summary = "获取预约列表", description = "根据条件查询预约列表")
    @GetMapping("/list")
    public ApiResponse<List<BookingVO>> getBookingList(BookingQueryRequest request) {
        List<BookingVO> list = bookingService.getBookingList(request);
        return ApiResponse.success(list);
    }

    @Operation(summary = "分页查询预约", description = "分页获取预约列表")
    @GetMapping("/page")
    public ApiResponse<Page<BookingVO>> getBookingPage(
            BookingQueryRequest request,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<BookingVO> page = bookingService.getBookingPage(request, pageNum, pageSize);
        return ApiResponse.success(page);
    }

    @Operation(summary = "我的预约", description = "获取当前用户的预约列表")
    @GetMapping("/my")
    public ApiResponse<List<BookingVO>> getMyBookings(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        BookingQueryRequest request = new BookingQueryRequest();
        request.setUserId(userId);
        List<BookingVO> list = bookingService.getBookingList(request);
        return ApiResponse.success(list);
    }

    @Operation(summary = "待审批预约", description = "获取所有待审批的预约列表")
    @GetMapping("/pending")
    public ApiResponse<List<BookingVO>> getPendingApprovals() {
        BookingQueryRequest request = new BookingQueryRequest();
        request.setStatus((byte) 0);
        List<BookingVO> list = bookingService.getBookingList(request);
        return ApiResponse.success(list);
    }
}
