package com.rosy.main.domain.dto.booking;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingAddRequest {

    private Long roomId;

    private Long userId;

    private String userName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String subject;
}
