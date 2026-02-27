package com.rosy.main.domain.dto.reservation;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ReservationApprovalRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "预约ID不能为空")
    private Long reservationId;

    @NotNull(message = "审批状态不能为空")
    @Min(value = 1, message = "审批状态只能为1（通过）或2（驳回）")
    @Max(value = 2, message = "审批状态只能为1（通过）或2（驳回）")
    private Byte approvalStatus;

    @Size(max = 500, message = "驳回原因长度不能超过500个字符")
    private String rejectReason;
}
