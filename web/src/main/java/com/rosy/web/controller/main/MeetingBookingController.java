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

@RestController
@RequestMapping("/meeting-booking")
public class MeetingBookingController {

    @Resource
    private IMeetingBookingService meetingBookingService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addBooking(@RequestBody MeetingBookingAddRequest request) {
        meetingBookingService.addBooking(request);
        return ApiResponse.success();
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteBooking(@RequestBody IdRequest idRequest) {
        boolean result = meetingBookingService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateBooking(@RequestBody MeetingBookingUpdateRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        meetingBookingService.updateBooking(request);
        return ApiResponse.success();
    }

    @PostMapping("/cancel")
    @ValidateRequest
    public ApiResponse cancelBooking(@RequestBody IdRequest idRequest) {
        MeetingBooking booking = meetingBookingService.getById(idRequest.getId());
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR, "预约记录不存在");

        booking.setStatus((byte) 3);
        boolean result = meetingBookingService.updateById(booking);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    public ApiResponse getBookingById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingBooking booking = meetingBookingService.getById(id);
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(booking);
    }

    @GetMapping("/get/vo")
    public ApiResponse getBookingVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingBooking booking = meetingBookingService.getById(id);
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(meetingBookingService.getBookingVO(booking));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listBookingByPage(@RequestBody MeetingBookingQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<MeetingBooking> page = meetingBookingService.page(
                new Page<>(current, size),
                meetingBookingService.getQueryWrapper(queryRequest)
        );
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listBookingVOByPage(@RequestBody MeetingBookingQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<MeetingBooking> page = meetingBookingService.page(
                new Page<>(current, size),
                meetingBookingService.getQueryWrapper(queryRequest)
        );
        Page<MeetingBookingVO> voPage = meetingBookingService.getBookingVOPage(page);
        return ApiResponse.success(voPage);
    }
}
