package com.rosy.main.domain.dto.bookingapproval;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BookingApprovalAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "预约ID不能为空")
    @Positive(message = "预约ID必须为正整数")
    private Long bookingId;

    @NotNull(message = "审批结果不能为空")
    @Min(value = 1, message = "审批结果只能为1(通过)或2(驳回)")
    @Max(value = 2, message = "审批结果只能为1(通过)或2(驳回)")
    private Byte status;

    @Size(max = 500, message = "审批意见长度不能超过500个字符")
    private String comment;
}
