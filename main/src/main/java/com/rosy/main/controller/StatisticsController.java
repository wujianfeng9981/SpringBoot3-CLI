package com.rosy.main.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.RoomUsageStatisticsVO;
import com.rosy.main.service.IRoomUsageStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "会议室使用统计", description = "会议室使用统计相关接口")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final IRoomUsageStatisticsService roomUsageStatisticsService;

    public StatisticsController(IRoomUsageStatisticsService roomUsageStatisticsService) {
        this.roomUsageStatisticsService = roomUsageStatisticsService;
    }

    @Operation(summary = "生成统计数据", description = "生成会议室使用统计数据")
    @PostMapping("/generate")
    public ApiResponse<Void> generateStatistics() {
        roomUsageStatisticsService.generateStatistics();
        return ApiResponse.success();
    }

    @Operation(summary = "获取日期范围统计", description = "获取指定日期范围内的会议室使用统计")
    @GetMapping("/date-range")
    public ApiResponse<List<RoomUsageStatisticsVO>> getStatisticsByDateRange(
            @Parameter(description = "开始日期，格式：YYYY-MM-DD") @RequestParam String startDate,
            @Parameter(description = "结束日期，格式：YYYY-MM-DD") @RequestParam String endDate) {
        List<RoomUsageStatisticsVO> list = roomUsageStatisticsService.getStatisticsByDateRange(
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
        return ApiResponse.success(list);
    }

    @Operation(summary = "获取会议室统计", description = "获取指定会议室在日期范围内的使用统计")
    @GetMapping("/room")
    public ApiResponse<List<RoomUsageStatisticsVO>> getStatisticsByRoomId(
            @Parameter(description = "会议室ID") @RequestParam Long roomId,
            @Parameter(description = "开始日期，格式：YYYY-MM-DD") @RequestParam String startDate,
            @Parameter(description = "结束日期，格式：YYYY-MM-DD") @RequestParam String endDate) {
        List<RoomUsageStatisticsVO> list = roomUsageStatisticsService.getStatisticsByRoomId(
                roomId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
        return ApiResponse.success(list);
    }

    @Operation(summary = "获取使用概况", description = "获取指定日期范围内的会议室使用概况")
    @GetMapping("/overview")
    public ApiResponse<List<RoomUsageStatisticsVO>> getUsageOverview(
            @Parameter(description = "开始日期，格式：YYYY-MM-DD") @RequestParam String startDate,
            @Parameter(description = "结束日期，格式：YYYY-MM-DD") @RequestParam String endDate) {
        List<RoomUsageStatisticsVO> list = roomUsageStatisticsService.getUsageOverview(
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
        return ApiResponse.success(list);
    }
}
