package com.rosy.main.domain.dto.reservation;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ReservationCancelRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "预约ID不能为空")
    private Long reservationId;

    @Size(max = 500, message = "取消原因长度不能超过500个字符")
    private String cancelReason;
}
