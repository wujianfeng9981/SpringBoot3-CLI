package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 签到实体类
 * 对应数据库表 sign_in
 * 
 * @author Rosy
 * @since 2025-02-27
 */
@Data
@TableName("sign_in")
public class SignIn implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 预约ID，关联reservation表
     */
    private Long reservationId;

    /**
     * 签到用户ID
     */
    private Long userId;

    /**
     * 签到状态：0-未签到，1-已签到，2-迟到，3-缺席
     * @see com.rosy.main.domain.enums.SignInStatus
     */
    private Byte signInStatus;

    /**
     * 签到时间
     */
    private LocalDateTime signInTime;

    /**
     * 备注
     */
    private String remark;

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
