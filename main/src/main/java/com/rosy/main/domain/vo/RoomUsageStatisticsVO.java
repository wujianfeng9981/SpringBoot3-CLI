package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RoomUsageStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long roomId;

    private String roomName;

    private Long totalReservations;

    private Long completedReservations;

    private Long cancelledReservations;

    private Double usageRate;

    private Double averageDuration;

    private Long totalSignInCount;

    private Long totalAbsentCount;
}
