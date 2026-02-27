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
import com.rosy.main.domain.dto.room.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.room.MeetingRoomQueryRequest;
import com.rosy.main.domain.dto.room.MeetingRoomUpdateRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.service.IMeetingRoomService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class MeetingRoomController {

    @Resource
    private IMeetingRoomService meetingRoomService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRoom(@RequestBody MeetingRoomAddRequest addRequest) {
        MeetingRoom room = BeanUtil.copyProperties(addRequest, MeetingRoom.class);
        boolean result = meetingRoomService.save(room);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(room.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteRoom(@RequestBody IdRequest idRequest) {
        boolean result = meetingRoomService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateRoom(@RequestBody MeetingRoomUpdateRequest updateRequest) {
        if (updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingRoom room = BeanUtil.copyProperties(updateRequest, MeetingRoom.class);
        boolean result = meetingRoomService.updateById(room);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    public ApiResponse getRoomById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingRoom room = meetingRoomService.getById(id);
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(room);
    }

    @GetMapping("/get/vo")
    public ApiResponse getRoomVOById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingRoom room = meetingRoomService.getById(id);
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(meetingRoomService.getMeetingRoomVO(room));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listRoomByPage(@RequestBody MeetingRoomQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<MeetingRoom> roomPage = meetingRoomService.page(
                new Page<>(current, size),
                meetingRoomService.getQueryWrapper(queryRequest)
        );
        return ApiResponse.success(roomPage);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listRoomVOByPage(@RequestBody MeetingRoomQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<MeetingRoom> roomPage = meetingRoomService.page(
                new Page<>(current, size),
                meetingRoomService.getQueryWrapper(queryRequest)
        );
        Page<MeetingRoomVO> roomVOPage = PageUtils.convert(roomPage, meetingRoomService::getMeetingRoomVO);
        return ApiResponse.success(roomVOPage);
    }
}
