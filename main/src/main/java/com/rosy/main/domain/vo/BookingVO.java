package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingVO {

    private Long id;

    private Long roomId;

    private String roomName;

    private Long userId;

    private String userName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String subject;

    private Byte status;

    private String statusText;

    private String rejectReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
