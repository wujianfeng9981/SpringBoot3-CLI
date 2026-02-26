package com.rosy.main.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomQueryRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomUpdateRequest;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.service.IMeetingRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "会议室管理", description = "会议室的增删改查操作接口")
@RestController
@RequestMapping("/api/meeting-room")
public class MeetingRoomController {

    private final IMeetingRoomService meetingRoomService;

    public MeetingRoomController(IMeetingRoomService meetingRoomService) {
        this.meetingRoomService = meetingRoomService;
    }

    @Operation(summary = "创建会议室", description = "创建新的会议室信息")
    @PostMapping
    public ApiResponse<Long> addMeetingRoom(@RequestBody MeetingRoomAddRequest request) {
        Long roomId = meetingRoomService.addRoom(request);
        return ApiResponse.success(roomId);
    }

    @Operation(summary = "更新会议室", description = "更新会议室的基本信息")
    @PutMapping
    public ApiResponse<Boolean> updateMeetingRoom(@RequestBody MeetingRoomUpdateRequest request) {
        Boolean result = meetingRoomService.updateRoom(request);
        return ApiResponse.success(result);
    }

    @Operation(summary = "删除会议室", description = "删除指定的会议室")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteMeetingRoom(
            @Parameter(description = "会议室ID") @PathVariable Long id) {
        Boolean result = meetingRoomService.deleteRoom(id);
        return ApiResponse.success(result);
    }

    @Operation(summary = "获取会议室详情", description = "根据ID获取会议室详细信息")
    @GetMapping("/{id}")
    public ApiResponse<MeetingRoomVO> getMeetingRoomById(
            @Parameter(description = "会议室ID") @PathVariable Long id) {
        MeetingRoomVO room = meetingRoomService.getRoomById(id);
        return ApiResponse.success(room);
    }

    @Operation(summary = "获取会议室列表", description = "根据条件查询会议室列表")
    @GetMapping
    public ApiResponse<List<MeetingRoomVO>> getMeetingRoomList(MeetingRoomQueryRequest request) {
        List<MeetingRoomVO> list = meetingRoomService.getRoomList(request);
        return ApiResponse.success(list);
    }

    @Operation(summary = "分页查询会议室", description = "分页获取会议室列表")
    @GetMapping("/page")
    public ApiResponse<Page<MeetingRoomVO>> getMeetingRoomPage(
            MeetingRoomQueryRequest request,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<MeetingRoomVO> page = meetingRoomService.getRoomPage(request, pageNum, pageSize);
        return ApiResponse.success(page);
    }
}
