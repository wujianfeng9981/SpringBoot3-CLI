package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairOrderAssignRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @NotNull(message = "工单ID不能为空")
    private Long orderId;

    /**
     * 维修人员ID
     */
    @NotNull(message = "维修人员ID不能为空")
    private Long repairmanId;

    /**
     * 分配方式：0-自动分配，1-手动分配
     */
    @NotNull(message = "分配方式不能为空")
    @Min(value = 0, message = "分配方式值只能为 0 或 1")
    @Max(value = 1, message = "分配方式值只能为 0 或 1")
    private Byte assignType;
}
