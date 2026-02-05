package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairRatingAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @NotNull(message = "工单ID不能为空")
    private Long orderId;

    /**
     * 评分：1-5星
     */
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分不能小于 1")
    @Max(value = 5, message = "评分不能大于 5")
    private Byte rating;

    /**
     * 评价内容
     */
    @Size(max = 500, message = "评价内容长度不能超过 500 个字符")
    private String content;

    /**
     * 服务态度评分：1-5星
     */
    @Min(value = 1, message = "服务态度评分不能小于 1")
    @Max(value = 5, message = "服务态度评分不能大于 5")
    private Byte attitudeRating;

    /**
     * 维修质量评分：1-5星
     */
    @Min(value = 1, message = "维修质量评分不能小于 1")
    @Max(value = 5, message = "维修质量评分不能大于 5")
    private Byte qualityRating;

    /**
     * 响应速度评分：1-5星
     */
    @Min(value = 1, message = "响应速度评分不能小于 1")
    @Max(value = 5, message = "响应速度评分不能大于 5")
    private Byte speedRating;
}
