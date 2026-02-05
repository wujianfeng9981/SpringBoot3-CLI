package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairOrderAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 设备类型：1-电脑，2-打印机，3-网络设备，4-其他
     */
    @NotNull(message = "设备类型不能为空")
    @Min(value = 1, message = "设备类型值不能小于 1")
    @Max(value = 4, message = "设备类型值不能大于 4")
    private Byte deviceType;

    /**
     * 故障位置
     */
    @NotBlank(message = "故障位置不能为空")
    @Size(max = 200, message = "故障位置长度不能超过 200 个字符")
    private String location;

    /**
     * 故障类型：1-硬件故障，2-软件故障，3-网络故障，4-其他
     */
    @NotNull(message = "故障类型不能为空")
    @Min(value = 1, message = "故障类型值不能小于 1")
    @Max(value = 4, message = "故障类型值不能大于 4")
    private Byte faultType;

    /**
     * 故障描述
     */
    @NotBlank(message = "故障描述不能为空")
    @Size(max = 1000, message = "故障描述长度不能超过 1000 个字符")
    private String description;

    /**
     * 故障照片URL，多个用逗号分隔
     */
    @Size(max = 500, message = "图片URL长度不能超过 500 个字符")
    private String images;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    @Min(value = 1, message = "优先级值不能小于 1")
    @Max(value = 4, message = "优先级值不能大于 4")
    private Byte priority;
}
