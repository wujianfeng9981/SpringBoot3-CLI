package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;

public interface IRepairOrderService extends IService<RepairOrder> {

    RepairOrderVO getRepairOrderVO(RepairOrder repairOrder);

    IPage<RepairOrderVO> getRepairOrderVOPage(Page<RepairOrder> page);

    LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest request);

    Long createRepairOrder(RepairOrderAddRequest request);

    Boolean updateRepairOrder(RepairOrderUpdateRequest request);

    Boolean assignRepairOrder(RepairOrderAssignRequest request);

    Boolean acceptRepairOrder(Long orderId);

    Boolean completeRepairOrder(Long orderId);

    Boolean cancelRepairOrder(Long orderId);
}
