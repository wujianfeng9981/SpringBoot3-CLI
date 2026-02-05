package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RepairOrderVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    private Long id;

    /**
     * 工单编号
     */
    private String orderNo;

    /**
     * 报修用户ID
     */
    private Long userId;

    /**
     * 设备类型：1-电脑，2-打印机，3-网络设备，4-其他
     */
    private Byte deviceType;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 故障位置
     */
    private String location;

    /**
     * 故障类型：1-硬件故障，2-软件故障，3-网络故障，4-其他
     */
    private Byte faultType;

    /**
     * 故障类型名称
     */
    private String faultTypeName;

    /**
     * 故障描述
     */
    private String description;

    /**
     * 故障照片URL，多个用逗号分隔
     */
    private String images;

    /**
     * 工单状态：0-待处理，1-已分配，2-维修中，3-已完成，4-已取消
     */
    private Byte status;

    /**
     * 工单状态名称
     */
    private String statusName;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    private Byte priority;

    /**
     * 优先级名称
     */
    private String priorityName;

    /**
     * 分配方式：0-自动分配，1-手动分配
     */
    private Byte assignType;

    /**
     * 分配方式名称
     */
    private String assignTypeName;

    /**
     * 维修人员ID
     */
    private Long repairmanId;

    /**
     * 维修人员名称
     */
    private String repairmanName;

    /**
     * 分配时间
     */
    private LocalDateTime assignedTime;

    /**
     * 完成时间
     */
    private LocalDateTime completedTime;

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
