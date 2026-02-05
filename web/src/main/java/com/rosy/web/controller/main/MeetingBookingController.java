package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingAddRequest;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingQueryRequest;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingUpdateRequest;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.vo.MeetingBookingVO;
import com.rosy.main.service.IMeetingBookingService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会议预约表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/meeting-booking")
public class MeetingBookingController {
    @Resource
    IMeetingBookingService meetingBookingService;

    /**
     * 创建预约
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addBooking(@RequestBody MeetingBookingAddRequest request) {
        Long id = meetingBookingService.addBooking(request);
        return ApiResponse.success(id);
    }

    /**
     * 取消预约
     */
    @PostMapping("/cancel")
    @ValidateRequest
    public ApiResponse cancelBooking(@RequestBody IdRequest idRequest) {
        boolean result = meetingBookingService.cancelBooking(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新预约
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateBooking(@RequestBody MeetingBookingUpdateRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = meetingBookingService.updateBooking(request);
        return ApiResponse.success(result);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    public ApiResponse getBookingById(@RequestParam("id") Long id) {
        MeetingBooking booking = meetingBookingService.getById(id);
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR);
        MeetingBookingVO bookingVO = meetingBookingService.getBookingVO(booking);
        return ApiResponse.success(bookingVO);
    }

    /**
     * 分页获取列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listBookingByPage(@RequestBody MeetingBookingQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<MeetingBooking> bookingPage = meetingBookingService.page(
                new Page<>(current, size),
                meetingBookingService.getQueryWrapper(request)
        );
        Page<MeetingBookingVO> bookingVOPage = meetingBookingService.getBookingVOPage(bookingPage);
        return ApiResponse.success(bookingVOPage);
    }
}
