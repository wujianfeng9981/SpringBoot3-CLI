package com.rosy.main.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
public class BookingApprovalRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "预约ID不能为空")
    private Long bookingId;

    @NotNull(message = "审批操作不能为空")
    private Byte action;

    private String comment;
}
