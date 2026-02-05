package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;

/**
 * <p>
 * 报修工单表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface IRepairOrderService extends IService<RepairOrder> {

    /**
     * 获取工单VO
     */
    RepairOrderVO getRepairOrderVO(RepairOrder repairOrder);

    /**
     * 获取工单VO分页
     */
    IPage<RepairOrderVO> getRepairOrderVOPage(Page<RepairOrder> page);

    /**
     * 获取查询条件
     */
    LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest queryRequest);

    /**
     * 分配工单
     */
    boolean assignOrder(Long orderId, Long repairmanId, Byte assignType);

    /**
     * 接单
     */
    boolean acceptOrder(Long orderId, Long repairmanId);

    /**
     * 开始维修
     */
    boolean startRepair(Long orderId);

    /**
     * 完成维修
     */
    boolean completeOrder(Long orderId);
}
