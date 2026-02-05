package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RepairRecordVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    private Long id;

    /**
     * 工单ID
     */
    private Long orderId;

    /**
     * 维修人员ID
     */
    private Long repairmanId;

    /**
     * 维修人员名称
     */
    private String repairmanName;

    /**
     * 操作类型：1-接单，2-开始维修，3-完成维修，4-转派，5-备注
     */
    private Byte actionType;

    /**
     * 操作类型名称
     */
    private String actionTypeName;

    /**
     * 操作内容描述
     */
    private String content;

    /**
     * 维修前照片URL，多个用逗号分隔
     */
    private String beforeImages;

    /**
     * 维修后照片URL，多个用逗号分隔
     */
    private String afterImages;

    /**
     * 使用的配件/材料
     */
    private String materials;

    /**
     * 维修费用
     */
    private BigDecimal cost;

    /**
     * 是否通知用户：0-未通知，1-已通知
     */
    private Byte notifyUser;

    /**
     * 通知内容
     */
    private String notifyContent;

    /**
     * 通知时间
     */
    private LocalDateTime notifyTime;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
