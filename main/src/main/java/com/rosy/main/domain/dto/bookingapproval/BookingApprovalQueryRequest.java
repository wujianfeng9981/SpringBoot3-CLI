package com.rosy.main.domain.dto.bookingapproval;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingApprovalQueryRequest extends PageRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 预约ID
     */
    private Long bookingId;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批状态：1-通过，2-驳回
     */
    private Byte status;
}
