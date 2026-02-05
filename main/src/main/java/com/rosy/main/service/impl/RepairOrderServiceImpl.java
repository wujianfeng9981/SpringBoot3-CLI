package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.service.IRepairOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    @Override
    public RepairOrderVO getRepairOrderVO(RepairOrder repairOrder) {
        return Optional.ofNullable(repairOrder)
                .map(order -> BeanUtil.copyProperties(order, RepairOrderVO.class))
                .orElse(null);
    }

    @Override
    public IPage<RepairOrderVO> getRepairOrderVOPage(Page<RepairOrder> page) {
        return page.convert(this::getRepairOrderVO);
    }

    @Override
    public LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<RepairOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(request.getId() != null, RepairOrder::getId, request.getId())
                .eq(request.getOrderNo() != null, RepairOrder::getOrderNo, request.getOrderNo())
                .like(request.getDeviceType() != null, RepairOrder::getDeviceType, request.getDeviceType())
                .like(request.getDeviceLocation() != null, RepairOrder::getDeviceLocation, request.getDeviceLocation())
                .like(request.getFaultType() != null, RepairOrder::getFaultType, request.getFaultType())
                .eq(request.getStatus() != null, RepairOrder::getStatus, request.getStatus())
                .eq(request.getPriority() != null, RepairOrder::getPriority, request.getPriority())
                .eq(request.getCreatorId() != null, RepairOrder::getCreatorId, request.getCreatorId())
                .like(request.getCreatorName() != null, RepairOrder::getCreatorName, request.getCreatorName())
                .eq(request.getAssigneeId() != null, RepairOrder::getAssigneeId, request.getAssigneeId())
                .like(request.getAssigneeName() != null, RepairOrder::getAssigneeName, request.getAssigneeName())
                .orderByDesc(RepairOrder::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRepairOrder(RepairOrderAddRequest request) {
        RepairOrder repairOrder = BeanUtil.copyProperties(request, RepairOrder.class);
        String orderNo = "RO" + System.currentTimeMillis() + RandomUtil.randomNumbers(4);
        repairOrder.setOrderNo(orderNo);
        repairOrder.setStatus(0);
        boolean result = this.save(repairOrder);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建报修工单失败");
        return repairOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRepairOrder(RepairOrderUpdateRequest request) {
        RepairOrder existingOrder = this.getById(request.getId());
        ThrowUtils.throwIf(existingOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        ThrowUtils.throwIf(existingOrder.getStatus() != 0, ErrorCode.OPERATION_ERROR, "只能修改待处理状态的工单");
        RepairOrder repairOrder = BeanUtil.copyProperties(request, RepairOrder.class);
        return this.updateById(repairOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignRepairOrder(RepairOrderAssignRequest request) {
        RepairOrder existingOrder = this.getById(request.getOrderId());
        ThrowUtils.throwIf(existingOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        ThrowUtils.throwIf(existingOrder.getStatus() != 0, ErrorCode.OPERATION_ERROR, "只能分配待处理状态的工单");
        existingOrder.setAssigneeId(request.getAssigneeId());
        existingOrder.setAssigneeName(request.getAssigneeName());
        existingOrder.setAssignType(request.getAssignType());
        existingOrder.setAssignTime(LocalDateTime.now());
        existingOrder.setStatus(1);
        return this.updateById(existingOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean acceptRepairOrder(Long orderId) {
        RepairOrder existingOrder = this.getById(orderId);
        ThrowUtils.throwIf(existingOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        ThrowUtils.throwIf(existingOrder.getStatus() != 1, ErrorCode.OPERATION_ERROR, "只能接受已分配状态的工单");
        existingOrder.setAcceptTime(LocalDateTime.now());
        existingOrder.setStatus(2);
        return this.updateById(existingOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeRepairOrder(Long orderId) {
        RepairOrder existingOrder = this.getById(orderId);
        ThrowUtils.throwIf(existingOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        ThrowUtils.throwIf(existingOrder.getStatus() != 2, ErrorCode.OPERATION_ERROR, "只能完成维修中状态的工单");
        existingOrder.setCompleteTime(LocalDateTime.now());
        existingOrder.setStatus(3);
        return this.updateById(existingOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelRepairOrder(Long orderId) {
        RepairOrder existingOrder = this.getById(orderId);
        ThrowUtils.throwIf(existingOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        ThrowUtils.throwIf(existingOrder.getStatus() == 3, ErrorCode.OPERATION_ERROR, "已完成的工单不能取消");
        existingOrder.setStatus(-1);
        return this.updateById(existingOrder);
    }
}
