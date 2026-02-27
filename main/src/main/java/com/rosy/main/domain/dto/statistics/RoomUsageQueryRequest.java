package com.rosy.main.domain.dto.statistics;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RoomUsageQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long roomId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
