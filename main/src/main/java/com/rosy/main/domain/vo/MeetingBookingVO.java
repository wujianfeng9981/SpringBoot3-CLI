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

    private Long userId;

    private String title;

    private String description;

    private Integer attendees;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Byte status;

    private String statusName;

    private Long creatorId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
