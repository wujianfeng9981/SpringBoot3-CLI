package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.service.IRepairOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * 报修工单表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    @Override
    public RepairOrderVO getRepairOrderVO(RepairOrder repairOrder) {
        if (repairOrder == null) {
            return null;
        }
        RepairOrderVO vo = BeanUtil.copyProperties(repairOrder, RepairOrderVO.class);
        // 设置枚举值对应的名称
        vo.setDeviceTypeName(getDeviceTypeName(repairOrder.getDeviceType()));
        vo.setFaultTypeName(getFaultTypeName(repairOrder.getFaultType()));
        vo.setStatusName(getStatusName(repairOrder.getStatus()));
        vo.setPriorityName(getPriorityName(repairOrder.getPriority()));
        vo.setAssignTypeName(getAssignTypeName(repairOrder.getAssignType()));
        return vo;
    }

    @Override
    public IPage<RepairOrderVO> getRepairOrderVOPage(Page<RepairOrder> page) {
        return page.convert(this::getRepairOrderVO);
    }

    @Override
    public LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<RepairOrder> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), RepairOrder::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getOrderNo(), RepairOrder::getOrderNo);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), RepairOrder::getUserId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getDeviceType(), RepairOrder::getDeviceType);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getLocation(), RepairOrder::getLocation);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getFaultType(), RepairOrder::getFaultType);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getStatus(), RepairOrder::getStatus);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getPriority(), RepairOrder::getPriority);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getAssignType(), RepairOrder::getAssignType);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getRepairmanId(), RepairOrder::getRepairmanId);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                RepairOrder::getId);

        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignOrder(Long orderId, Long repairmanId, Byte assignType) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不允许分配");
        }
        order.setRepairmanId(repairmanId);
        order.setAssignType(assignType);
        order.setAssignedTime(LocalDateTime.now());
        order.setStatus((byte) 1);
        return updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean acceptOrder(Long orderId, Long repairmanId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不允许接单");
        }
        if (!order.getRepairmanId().equals(repairmanId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权操作此工单");
        }
        order.setStatus((byte) 2);
        return updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startRepair(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不允许开始维修");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeOrder(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不允许完成");
        }
        order.setStatus((byte) 3);
        order.setCompletedTime(LocalDateTime.now());
        return updateById(order);
    }

    private String getDeviceTypeName(Byte deviceType) {
        if (deviceType == null) return "";
        return switch (deviceType) {
            case 1 -> "电脑";
            case 2 -> "打印机";
            case 3 -> "网络设备";
            case 4 -> "其他";
            default -> "";
        };
    }

    private String getFaultTypeName(Byte faultType) {
        if (faultType == null) return "";
        return switch (faultType) {
            case 1 -> "硬件故障";
            case 2 -> "软件故障";
            case 3 -> "网络故障";
            case 4 -> "其他";
            default -> "";
        };
    }

    private String getStatusName(Byte status) {
        if (status == null) return "";
        return switch (status) {
            case 0 -> "待处理";
            case 1 -> "已分配";
            case 2 -> "维修中";
            case 3 -> "已完成";
            case 4 -> "已取消";
            default -> "";
        };
    }

    private String getPriorityName(Byte priority) {
        if (priority == null) return "";
        return switch (priority) {
            case 1 -> "低";
            case 2 -> "中";
            case 3 -> "高";
            case 4 -> "紧急";
            default -> "";
        };
    }

    private String getAssignTypeName(Byte assignType) {
        if (assignType == null) return "";
        return switch (assignType) {
            case 0 -> "自动分配";
            case 1 -> "手动分配";
            default -> "";
        };
    }
}
