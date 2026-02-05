package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairOrderAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "设备类型不能为空")
    @Size(max = 50, message = "设备类型长度不能超过50个字符")
    private String deviceType;

    @NotBlank(message = "设备位置不能为空")
    @Size(max = 100, message = "设备位置长度不能超过100个字符")
    private String deviceLocation;

    @NotBlank(message = "故障类型不能为空")
    @Size(max = 50, message = "故障类型长度不能超过50个字符")
    private String faultType;

    @Size(max = 500, message = "故障描述长度不能超过500个字符")
    private String faultDescription;

    @Size(max = 500, message = "图片路径长度不能超过500个字符")
    private String faultImages;

    @NotNull(message = "优先级不能为空")
    @Min(value = 1, message = "优先级范围为1-3")
    @Max(value = 3, message = "优先级范围为1-3")
    private Integer priority;
}
