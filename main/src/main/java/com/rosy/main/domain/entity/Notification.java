package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知实体类
 * 对应数据库表 notification
 * 
 * @author Rosy
 * @since 2025-02-27
 */
@Data
@TableName("notification")
public class Notification implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接收通知的用户ID
     */
    private Long userId;

    /**
     * 关联的预约ID
     */
    private Long reservationId;

    /**
     * 通知类型：0-审批结果通知，1-会议提醒，2-取消通知
     * @see com.rosy.main.domain.enums.NotificationType
     */
    private Byte type;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 是否已读：0-未读，1-已读
     */
    private Byte isRead;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

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
     * 逻辑删除标记：0-未删除，1-已删除
     */
    @TableLogic
    private Byte isDeleted;
}
