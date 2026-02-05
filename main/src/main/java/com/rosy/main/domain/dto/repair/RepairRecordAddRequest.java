package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairRecordAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    @Positive(message = "工单ID必须为正整数")
    private Long orderId;

    @NotNull(message = "操作类型不能为空")
    @Min(value = 1, message = "操作类型范围为1-10")
    @Max(value = 10, message = "操作类型范围为1-10")
    private Integer actionType;

    @NotBlank(message = "操作描述不能为空")
    @Size(max = 500, message = "操作描述长度不能超过500个字符")
    private String actionDescription;

    @Size(max = 500, message = "图片路径长度不能超过500个字符")
    private String images;

    @Min(value = 1, message = "结果范围为1-3")
    @Max(value = 3, message = "结果范围为1-3")
    private Integer result;

    @Size(max = 500, message = "结果描述长度不能超过500个字符")
    private String resultDescription;
}
