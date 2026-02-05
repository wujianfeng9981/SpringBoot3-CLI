package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.meetingcheckin.MeetingCheckinAddRequest;
import com.rosy.main.domain.dto.meetingcheckin.MeetingCheckinQueryRequest;
import com.rosy.main.domain.entity.MeetingCheckin;
import com.rosy.main.domain.vo.MeetingCheckinVO;
import com.rosy.main.domain.vo.MeetingRoomStatisticsVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 会议签到表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface IMeetingCheckinService extends IService<MeetingCheckin> {

    /**
     * 签到
     *
     * @param request 签到请求
     * @return 签到ID
     */
    Long checkin(MeetingCheckinAddRequest request);

    /**
     * 获取签到VO
     *
     * @param meetingCheckin 签到实体
     * @return 签到VO
     */
    MeetingCheckinVO getCheckinVO(MeetingCheckin meetingCheckin);

    /**
     * 获取签到VO分页
     *
     * @param page 分页对象
     * @return VO分页对象
     */
    Page<MeetingCheckinVO> getCheckinVOPage(Page<MeetingCheckin> page);

    /**
     * 获取查询条件包装器
     *
     * @param queryRequest 查询请求
     * @return 查询条件包装器
     */
    LambdaQueryWrapper<MeetingCheckin> getQueryWrapper(MeetingCheckinQueryRequest queryRequest);

    /**
     * 获取会议室使用统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计数据列表
     */
    List<MeetingRoomStatisticsVO> getRoomStatistics(LocalDateTime startTime, LocalDateTime endTime);
}
