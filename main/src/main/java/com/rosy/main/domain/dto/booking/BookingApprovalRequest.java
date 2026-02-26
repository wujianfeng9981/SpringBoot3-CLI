package com.rosy.main.domain.dto.booking;

import lombok.Data;

@Data
public class BookingApprovalRequest {

    private Long bookingId;

    private Long approverId;

    private Byte approvalResult;

    private String comment;
}
