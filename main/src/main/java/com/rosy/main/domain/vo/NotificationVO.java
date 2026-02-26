package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationVO {

    private Long id;

    private Long userId;

    private Long bookingId;

    private Byte type;

    private String typeText;

    private String title;

    private String content;

    private Byte isRead;

    private LocalDateTime sendTime;
}
