package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.MeetingRoomUpdateRequest;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.service.IMeetingRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meeting-room")
@Tag(name = "会议室管理")
public class MeetingRoomController {

    @Resource
    private IMeetingRoomService meetingRoomService;

    @PostMapping("/add")
    @Operation(summary = "添加会议室")
    public ApiResponse addRoom(@Valid @RequestBody MeetingRoomAddRequest request) {
        Long id = meetingRoomService.addRoom(request);
        return ApiResponse.success(id);
    }

    @PostMapping("/update")
    @Operation(summary = "更新会议室")
    public ApiResponse updateRoom(@Valid @RequestBody MeetingRoomUpdateRequest request) {
        meetingRoomService.updateRoom(request);
        return ApiResponse.success();
    }

    @PostMapping("/delete")
    @Operation(summary = "删除会议室")
    public ApiResponse deleteRoom(@Valid @RequestBody IdRequest request) {
        meetingRoomService.deleteRoom(request.getId());
        return ApiResponse.success();
    }

    @GetMapping("/get")
    @Operation(summary = "获取会议室详情")
    public ApiResponse getRoomById(@RequestParam Long id) {
        MeetingRoomVO vo = meetingRoomService.getRoomById(id);
        return ApiResponse.success(vo);
    }

    @GetMapping("/list")
    @Operation(summary = "获取会议室列表")
    public ApiResponse listRooms(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Byte status) {
        Page<MeetingRoomVO> page = meetingRoomService.listRooms(current, size, keyword, status);
        return ApiResponse.success(page);
    }
}
