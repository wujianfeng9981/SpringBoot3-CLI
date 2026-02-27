package com.rosy.main.modules.meeting.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.modules.meeting.dto.MeetingRoomDTO;
import com.rosy.main.modules.meeting.service.IMeetingRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会议室管理控制器
 * 提供会议室的增删改查接口
 */
@RestController
@RequestMapping("/meeting/rooms")
@RequiredArgsConstructor
public class MeetingRoomController {

    private final IMeetingRoomService meetingRoomService;

    /**
     * 创建会议室
     *
     * @param dto 会议室信息
     * @return 创建的会议室
     */
    @PostMapping
    public ApiResponse createRoom(@Valid @RequestBody MeetingRoomDTO dto) {
        return ApiResponse.success("会议室创建成功", meetingRoomService.createRoom(dto));
    }

    /**
     * 根据ID查询会议室
     *
     * @param id 会议室ID
     * @return 会议室信息
     */
    @GetMapping("/{id}")
    public ApiResponse getRoomById(@PathVariable Long id) {
        return ApiResponse.success(meetingRoomService.getRoomById(id));
    }

    /**
     * 查询所有会议室
     *
     * @return 会议室列表
     */
    @GetMapping
    public ApiResponse getAllRooms() {
        return ApiResponse.success(meetingRoomService.getAllRooms());
    }

    /**
     * 查询所有启用的会议室
     *
     * @return 启用的会议室列表
     */
    @GetMapping("/active")
    public ApiResponse getActiveRooms() {
        return ApiResponse.success(meetingRoomService.getActiveRooms());
    }

    /**
     * 根据最小容量查询会议室
     *
     * @param minCapacity 最小容量
     * @return 符合条件的会议室列表
     */
    @GetMapping("/capacity/{minCapacity}")
    public ApiResponse getRoomsByCapacity(@PathVariable Integer minCapacity) {
        return ApiResponse.success(meetingRoomService.getRoomsByCapacity(minCapacity));
    }

    /**
     * 更新会议室信息
     *
     * @param id  会议室ID
     * @param dto 更新的信息
     * @return 更新后的会议室
     */
    @PutMapping("/{id}")
    public ApiResponse updateRoom(@PathVariable Long id, @Valid @RequestBody MeetingRoomDTO dto) {
        return ApiResponse.success("会议室更新成功", meetingRoomService.updateRoom(id, dto));
    }

    /**
     * 停用会议室
     *
     * @param id 会议室ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse deactivateRoom(@PathVariable Long id) {
        meetingRoomService.deactivateRoom(id);
        return ApiResponse.success("会议室已停用");
    }

    /**
     * 启用会议室
     *
     * @param id 会议室ID
     * @return 操作结果
     */
    @PostMapping("/{id}/activate")
    public ApiResponse activateRoom(@PathVariable Long id) {
        meetingRoomService.activateRoom(id);
        return ApiResponse.success("会议室已启用");
    }
}
