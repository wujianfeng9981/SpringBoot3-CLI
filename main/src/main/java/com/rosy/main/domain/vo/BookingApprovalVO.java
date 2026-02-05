package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BookingApprovalVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long bookingId;

    private Long approverId;

    private String approverName;

    private Byte action;

    private String comment;

    private LocalDateTime approvalTime;

    private LocalDateTime createTime;
}
