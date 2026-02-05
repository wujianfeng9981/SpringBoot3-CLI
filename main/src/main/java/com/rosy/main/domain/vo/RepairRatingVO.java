package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RepairRatingVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评价ID
     */
    private Long id;

    /**
     * 工单ID
     */
    private Long orderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 维修人员ID
     */
    private Long repairmanId;

    /**
     * 维修人员名称
     */
    private String repairmanName;

    /**
     * 评分：1-5星
     */
    private Byte rating;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 服务态度评分：1-5星
     */
    private Byte attitudeRating;

    /**
     * 维修质量评分：1-5星
     */
    private Byte qualityRating;

    /**
     * 响应速度评分：1-5星
     */
    private Byte speedRating;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
