package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MeetingRoomStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long roomId;

    private String roomName;

    private Long totalBookings;

    private Long approvedBookings;

    private Long rejectedBookings;

    private Long cancelledBookings;

    private Long totalCheckins;

    private BigDecimal utilizationRate;

    private BigDecimal averageAttendees;
}
