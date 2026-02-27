package com.rosy.main.service;

import com.rosy.main.domain.dto.statistics.RoomUsageQueryRequest;
import com.rosy.main.domain.vo.RoomUsageStatisticsVO;

import java.util.List;

public interface IStatisticsService {

    RoomUsageStatisticsVO getRoomUsageStatistics(Long roomId, RoomUsageQueryRequest queryRequest);

    List<RoomUsageStatisticsVO> getAllRoomsUsageStatistics(RoomUsageQueryRequest queryRequest);
}
