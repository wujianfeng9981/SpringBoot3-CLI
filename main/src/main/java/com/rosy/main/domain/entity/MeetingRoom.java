package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会议室实体类
 * 对应数据库表 meeting_room
 * 
 * @author Rosy
 * @since 2025-02-27
 */
@Data
@TableName("meeting_room")
public class MeetingRoom implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会议室名称
     */
    private String name;

    /**
     * 会议室位置/地址
     */
    private String location;

    /**
     * 会议室容量（人数）
     */
    private Integer capacity;

    /**
     * 设备信息（JSON格式，如投影仪、白板等）
     */
    private String equipment;

    /**
     * 状态：0-可用，1-维护中，2-已停用
     * @see com.rosy.main.domain.enums.RoomStatus
     */
    private Byte status;

    /**
     * 会议室描述
     */
    private String description;

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
