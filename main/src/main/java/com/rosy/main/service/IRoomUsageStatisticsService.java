package com.rosy.main.service;

import com.rosy.main.domain.vo.RoomUsageStatisticsVO;

import java.time.LocalDate;
import java.util.List;

public interface IRoomUsageStatisticsService {

    /**
     * 生成会议室使用统计数据
     */
    void generateStatistics();

    /**
     * 获取会议室使用统计（按日期）
     */
    List<RoomUsageStatisticsVO> getStatisticsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * 获取会议室使用统计（按会议室）
     */
    List<RoomUsageStatisticsVO> getStatisticsByRoomId(Long roomId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取会议室使用概况
     */
    List<RoomUsageStatisticsVO> getUsageOverview(LocalDate startDate, LocalDate endDate);
}
