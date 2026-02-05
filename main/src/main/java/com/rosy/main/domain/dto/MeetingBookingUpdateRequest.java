package com.rosy.main.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MeetingBookingUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    private Long id;

    private Long roomId;

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String reason;

    private Integer attendees;
}
