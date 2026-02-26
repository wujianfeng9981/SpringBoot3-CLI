package com.rosy.main.domain.dto.booking;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingQueryRequest extends PageRequest {

    private Long roomId;

    private Long userId;

    private Byte status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
