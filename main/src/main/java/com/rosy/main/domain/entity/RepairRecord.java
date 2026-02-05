package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 维修记录表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
@TableName("repair_record")
public class RepairRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 操作类型：1-接单，2-开始维修，3-完成维修，4-转派，5-备注
     */
    private Byte actionType;

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
    private java.math.BigDecimal cost;

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
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 乐观锁版本号
     */
    @Version
    private Byte version;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Byte isDeleted;
}
