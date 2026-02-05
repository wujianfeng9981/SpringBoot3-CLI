package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class RepairOrderQueryRequest extends PageRequest {

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
     * 故障位置（模糊查询）
     */
    private String location;

    /**
     * 故障类型：1-硬件故障，2-软件故障，3-网络故障，4-其他
     */
    private Byte faultType;

    /**
     * 工单状态：0-待处理，1-已分配，2-维修中，3-已完成，4-已取消
     */
    private Byte status;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    private Byte priority;

    /**
     * 分配方式：0-自动分配，1-手动分配
     */
    private Byte assignType;

    /**
     * 维修人员ID
     */
    private Long repairmanId;
}
