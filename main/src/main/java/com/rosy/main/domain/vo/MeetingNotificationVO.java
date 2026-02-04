package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MeetingNotificationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long bookingId;

    private String meetingTitle;

    private Long userId;

    private Byte type;

    private String typeName;

    private String content;

    private Byte isRead;

    private LocalDateTime readTime;

    private LocalDateTime createTime;
}
