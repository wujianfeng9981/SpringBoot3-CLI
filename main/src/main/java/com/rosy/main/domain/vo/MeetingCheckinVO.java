package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MeetingCheckinVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long bookingId;

    private String meetingTitle;

    private Long userId;

    private String userName;

    private LocalDateTime checkinTime;

    private LocalDateTime createTime;
}
