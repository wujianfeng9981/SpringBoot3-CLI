package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RepairRecordQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Positive(message = "工单ID必须为正整数")
    private Long orderId;

    @Size(max = 50, message = "工单号长度不能超过50个字符")
    private String orderNo;

    @Min(value = 1, message = "操作类型范围为1-10")
    @Max(value = 10, message = "操作类型范围为1-10")
    private Integer actionType;

    @Positive(message = "创建者ID必须为正整数")
    private Long creatorId;
}
