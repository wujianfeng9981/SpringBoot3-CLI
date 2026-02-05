package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairOrderAssignRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    @Positive(message = "工单ID必须为正整数")
    private Long orderId;

    @NotNull(message = "指派人ID不能为空")
    @Positive(message = "指派人ID必须为正整数")
    private Long assigneeId;

    @NotBlank(message = "指派人姓名不能为空")
    @Size(max = 50, message = "指派人姓名长度不能超过50个字符")
    private String assigneeName;

    @NotNull(message = "分配类型不能为空")
    @Min(value = 1, message = "分配类型为1或2")
    @Max(value = 2, message = "分配类型为1或2")
    private Integer assignType;
}
