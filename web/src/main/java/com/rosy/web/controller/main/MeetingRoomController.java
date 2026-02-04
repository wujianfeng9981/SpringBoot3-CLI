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
import com.rosy.main.domain.dto.meetingroom.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomQueryRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomUpdateRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.service.IMeetingRoomService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meeting-room")
public class MeetingRoomController {

    @Resource
    private IMeetingRoomService meetingRoomService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addMeetingRoom(@RequestBody MeetingRoomAddRequest request) {
        MeetingRoom meetingRoom = new MeetingRoom();
        BeanUtils.copyProperties(request, meetingRoom);
        boolean result = meetingRoomService.save(meetingRoom);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(meetingRoom.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteMeetingRoom(@RequestBody IdRequest idRequest) {
        boolean result = meetingRoomService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateMeetingRoom(@RequestBody MeetingRoomUpdateRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingRoom meetingRoom = BeanUtil.copyProperties(request, MeetingRoom.class);
        boolean result = meetingRoomService.updateById(meetingRoom);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    public ApiResponse getMeetingRoomById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingRoom meetingRoom = meetingRoomService.getById(id);
        ThrowUtils.throwIf(meetingRoom == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(meetingRoom);
    }

    @GetMapping("/get/vo")
    public ApiResponse getMeetingRoomVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingRoom meetingRoom = meetingRoomService.getById(id);
        ThrowUtils.throwIf(meetingRoom == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(meetingRoomService.getMeetingRoomVO(meetingRoom));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listMeetingRoomByPage(@RequestBody MeetingRoomQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<MeetingRoom> page = meetingRoomService.page(
                new Page<>(current, size),
                meetingRoomService.getQueryWrapper(queryRequest)
        );
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listMeetingRoomVOByPage(@RequestBody MeetingRoomQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<MeetingRoom> page = meetingRoomService.page(
                new Page<>(current, size),
                meetingRoomService.getQueryWrapper(queryRequest)
        );
        Page<MeetingRoomVO> voPage = PageUtils.convert(page, meetingRoomService::getMeetingRoomVO);
        return ApiResponse.success(voPage);
    }
}
