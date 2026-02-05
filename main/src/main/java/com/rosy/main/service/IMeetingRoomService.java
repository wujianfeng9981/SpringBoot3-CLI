package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomQueryRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomUpdateRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;

/**
 * <p>
 * 会议室表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface IMeetingRoomService extends IService<MeetingRoom> {

    /**
     * 创建会议室
     *
     * @param addRequest 创建请求
     * @return 会议室ID
     */
    Long addMeetingRoom(MeetingRoomAddRequest addRequest);

    /**
     * 更新会议室
     *
     * @param updateRequest 更新请求
     * @return 是否成功
     */
    boolean updateMeetingRoom(MeetingRoomUpdateRequest updateRequest);

    /**
     * 获取会议室VO
     *
     * @param meetingRoom 会议室实体
     * @return 会议室VO
     */
    MeetingRoomVO getMeetingRoomVO(MeetingRoom meetingRoom);

    /**
     * 获取会议室VO分页
     *
     * @param page 分页对象
     * @return VO分页对象
     */
    Page<MeetingRoomVO> getMeetingRoomVOPage(Page<MeetingRoom> page);

    /**
     * 获取查询条件包装器
     *
     * @param queryRequest 查询请求
     * @return 查询条件包装器
     */
    LambdaQueryWrapper<MeetingRoom> getQueryWrapper(MeetingRoomQueryRequest queryRequest);
}
