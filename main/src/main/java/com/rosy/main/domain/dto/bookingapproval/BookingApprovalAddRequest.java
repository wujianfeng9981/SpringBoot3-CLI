package com.rosy.main.domain.dto.bookingapproval;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BookingApprovalAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    @NotNull(message = "预约ID不能为空")
    private Long bookingId;

    /**
     * 审批状态：1-通过，2-驳回
     */
    @NotNull(message = "审批状态不能为空")
    @Min(value = 1, message = "审批状态只能是 1（通过）或 2（驳回）")
    @Max(value = 2, message = "审批状态只能是 1（通过）或 2（驳回）")
    private Byte status;

    /**
     * 审批意见
     */
    @Size(max = 500, message = "审批意见长度不能超过 500 个字符")
    private String comment;
}
