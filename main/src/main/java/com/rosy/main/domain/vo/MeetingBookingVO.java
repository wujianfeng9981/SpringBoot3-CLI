package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MeetingBookingVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long roomId;

    private String roomName;

    private Long applicantId;

    private String applicantName;

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String reason;

    private Integer attendees;

    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
