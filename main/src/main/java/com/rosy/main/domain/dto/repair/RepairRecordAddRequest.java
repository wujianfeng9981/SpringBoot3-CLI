package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RepairRecordAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @NotNull(message = "工单ID不能为空")
    private Long orderId;

    /**
     * 操作类型：1-接单，2-开始维修，3-完成维修，4-转派，5-备注
     */
    @NotNull(message = "操作类型不能为空")
    @Min(value = 1, message = "操作类型值不能小于 1")
    @Max(value = 5, message = "操作类型值不能大于 5")
    private Byte actionType;

    /**
     * 操作内容描述
     */
    @NotBlank(message = "操作内容不能为空")
    @Size(max = 1000, message = "操作内容长度不能超过 1000 个字符")
    private String content;

    /**
     * 维修前照片URL，多个用逗号分隔
     */
    @Size(max = 500, message = "图片URL长度不能超过 500 个字符")
    private String beforeImages;

    /**
     * 维修后照片URL，多个用逗号分隔
     */
    @Size(max = 500, message = "图片URL长度不能超过 500 个字符")
    private String afterImages;

    /**
     * 使用的配件/材料
     */
    @Size(max = 500, message = "配件材料长度不能超过 500 个字符")
    private String materials;

    /**
     * 维修费用
     */
    @DecimalMin(value = "0.00", message = "维修费用不能小于0")
    @Digits(integer = 10, fraction = 2, message = "维修费用格式错误")
    private BigDecimal cost;

    /**
     * 是否通知用户：0-未通知，1-已通知
     */
    @Min(value = 0, message = "通知状态值只能为 0 或 1")
    @Max(value = 1, message = "通知状态值只能为 0 或 1")
    private Byte notifyUser;

    /**
     * 通知内容
     */
    @Size(max = 500, message = "通知内容长度不能超过 500 个字符")
    private String notifyContent;
}
