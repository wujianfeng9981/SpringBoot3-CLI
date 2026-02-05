package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RepairOrderQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Size(max = 50, message = "工单号长度不能超过50个字符")
    private String orderNo;

    @Size(max = 50, message = "设备类型长度不能超过50个字符")
    private String deviceType;

    @Size(max = 100, message = "设备位置长度不能超过100个字符")
    private String deviceLocation;

    @Size(max = 50, message = "故障类型长度不能超过50个字符")
    private String faultType;

    @Min(value = 0, message = "状态值不能小于0")
    @Max(value = 3, message = "状态值不能大于3")
    private Integer status;

    @Min(value = 1, message = "优先级范围为1-3")
    @Max(value = 3, message = "优先级范围为1-3")
    private Integer priority;

    @Positive(message = "创建者ID必须为正整数")
    private Long creatorId;

    @Size(max = 50, message = "创建者姓名长度不能超过50个字符")
    private String creatorName;

    @Positive(message = "指派人ID必须为正整数")
    private Long assigneeId;

    @Size(max = 50, message = "指派人姓名长度不能超过50个字符")
    private String assigneeName;
}
