package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ReservationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long roomId;

    private String roomName;

    private String roomLocation;

    private Long applicantId;

    private String applicantName;

    private String title;

    private String purpose;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Byte status;

    private String statusDesc;

    private String rejectReason;

    private Integer attendeeCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
