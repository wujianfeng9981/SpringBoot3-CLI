package com.rosy.main.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RoomUsageStatisticsVO {

    private Long id;

    private Long roomId;

    private String roomName;

    private LocalDate statisticsDate;

    private Integer totalBookings;

    private Integer approvedBookings;

    private Integer rejectedBookings;

    private Integer cancelledBookings;

    private Integer totalDuration;

    private Integer actualUsageDuration;

    private BigDecimal occupancyRate;

    private Integer checkInCount;
}
