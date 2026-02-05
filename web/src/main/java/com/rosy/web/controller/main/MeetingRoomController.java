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

/**
 * <p>
 * 会议室表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/meeting-room")
public class MeetingRoomController {
    @Resource
    IMeetingRoomService meetingRoomService;

    /**
     * 创建
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addMeetingRoom(@RequestBody MeetingRoomAddRequest meetingRoomAddRequest) {
        Long id = meetingRoomService.addMeetingRoom(meetingRoomAddRequest);
        return ApiResponse.success(id);
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteMeetingRoom(@RequestBody IdRequest idRequest) {
        boolean result = meetingRoomService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateMeetingRoom(@RequestBody MeetingRoomUpdateRequest meetingRoomUpdateRequest) {
        if (meetingRoomUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = meetingRoomService.updateMeetingRoom(meetingRoomUpdateRequest);
        return ApiResponse.success(result);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    public ApiResponse getMeetingRoomById(@RequestParam("id") Long id) {
        MeetingRoom meetingRoom = meetingRoomService.getById(id);
        ThrowUtils.throwIf(meetingRoom == null, ErrorCode.NOT_FOUND_ERROR);
        MeetingRoomVO meetingRoomVO = meetingRoomService.getMeetingRoomVO(meetingRoom);
        return ApiResponse.success(meetingRoomVO);
    }

    /**
     * 分页获取列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listMeetingRoomByPage(@RequestBody MeetingRoomQueryRequest meetingRoomQueryRequest) {
        long current = meetingRoomQueryRequest.getCurrent();
        long size = meetingRoomQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<MeetingRoom> meetingRoomPage = meetingRoomService.page(
                new Page<>(current, size),
                meetingRoomService.getQueryWrapper(meetingRoomQueryRequest)
        );
        Page<MeetingRoomVO> meetingRoomVOPage = meetingRoomService.getMeetingRoomVOPage(meetingRoomPage);
        return ApiResponse.success(meetingRoomVOPage);
    }
}
