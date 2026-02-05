package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class RepairRecordQueryRequest extends PageRequest {

    @Serial
    private static final long serialVersionUID = 1L;

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
}
