package com.rosy.main.modules.meeting.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.modules.meeting.dto.BookingDTO;
import com.rosy.main.modules.meeting.dto.BookingRequest;
import com.rosy.main.modules.meeting.enums.BookingStatus;
import com.rosy.main.modules.meeting.service.IBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预约管理控制器
 * 提供预约申请、查询、取消等接口
 */
@RestController
@RequestMapping("/meeting/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final IBookingService bookingService;

    /**
     * 创建预约申请
     *
     * @param request 预约申请信息
     * @return 创建的预约记录
     */
    @PostMapping
    public ApiResponse createBooking(@Valid @RequestBody BookingRequest request) {
        return ApiResponse.success("预约申请已提交", bookingService.createBooking(request));
    }

    /**
     * 根据ID查询预约记录
     *
     * @param id 预约ID
     * @return 预约记录
     */
    @GetMapping("/{id}")
    public ApiResponse getBookingById(@PathVariable Long id) {
        return ApiResponse.success(bookingService.getBookingById(id));
    }

    /**
     * 查询用户的所有预约记录
     *
     * @param userId 用户ID
     * @return 预约记录列表
     */
    @GetMapping("/user/{userId}")
    public ApiResponse getUserBookings(@PathVariable Long userId) {
        return ApiResponse.success(bookingService.getUserBookings(userId));
    }

    /**
     * 查询会议室的预约记录
     *
     * @param roomId 会议室ID
     * @param status 预约状态，默认为APPROVED
     * @return 预约记录列表
     */
    @GetMapping("/room/{roomId}")
    public ApiResponse getRoomBookings(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "APPROVED") BookingStatus status) {
        return ApiResponse.success(bookingService.getRoomBookings(roomId, status));
    }

    /**
     * 取消预约
     *
     * @param id     预约ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/{id}/cancel")
    public ApiResponse cancelBooking(@PathVariable Long id, @RequestParam Long userId) {
        bookingService.cancelBooking(id, userId);
        return ApiResponse.success("预约已取消");
    }

    /**
     * 审批预约
     *
     * @param id       预约ID
     * @param approved 是否通过
     * @param comment  审批意见
     * @return 操作结果
     */
    @PostMapping("/{id}/approve")
    public ApiResponse approveBooking(
            @PathVariable Long id,
            @RequestParam Boolean approved,
            @RequestParam(required = false) String comment) {
        bookingService.approveBooking(id, approved, comment);
        String message = Boolean.TRUE.equals(approved) ? "预约已通过" : "预约已驳回";
        return ApiResponse.success(message);
    }

    /**
     * 查询用户的即将开始的会议
     *
     * @param userId 用户ID
     * @return 即将开始的会议列表
     */
    @GetMapping("/user/{userId}/upcoming")
    public ApiResponse getUpcomingMeetings(@PathVariable Long userId) {
        return ApiResponse.success(bookingService.getUpcomingMeetings(userId));
    }
}
