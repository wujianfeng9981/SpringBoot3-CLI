package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.dto.repair.RepairRecordQueryRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;
import com.rosy.main.mapper.RepairRecordMapper;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RepairRecordServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements IRepairRecordService {

    @Resource
    private IRepairOrderService repairOrderService;

    @Override
    public RepairRecordVO getRepairRecordVO(RepairRecord repairRecord) {
        return Optional.ofNullable(repairRecord)
                .map(record -> BeanUtil.copyProperties(record, RepairRecordVO.class))
                .orElse(null);
    }

    @Override
    public IPage<RepairRecordVO> getRepairRecordVOPage(Page<RepairRecord> page) {
        return page.convert(this::getRepairRecordVO);
    }

    @Override
    public LambdaQueryWrapper<RepairRecord> getQueryWrapper(RepairRecordQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<RepairRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(request.getId() != null, RepairRecord::getId, request.getId())
                .eq(request.getOrderId() != null, RepairRecord::getOrderId, request.getOrderId())
                .eq(request.getOrderNo() != null, RepairRecord::getOrderNo, request.getOrderNo())
                .eq(request.getActionType() != null, RepairRecord::getActionType, request.getActionType())
                .eq(request.getCreatorId() != null, RepairRecord::getCreatorId, request.getCreatorId())
                .orderByDesc(RepairRecord::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRepairRecord(RepairRecordAddRequest request) {
        RepairOrder repairOrder = repairOrderService.getById(request.getOrderId());
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        RepairRecord repairRecord = BeanUtil.copyProperties(request, RepairRecord.class);
        repairRecord.setOrderNo(repairOrder.getOrderNo());
        boolean result = this.save(repairRecord);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建维修记录失败");
        return repairRecord.getId();
    }

    @Override
    public IPage<RepairRecordVO> getRecordsByOrderId(Long orderId, long current, long size) {
        Page<RepairRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<RepairRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairRecord::getOrderId, orderId)
                .orderByDesc(RepairRecord::getCreateTime);
        Page<RepairRecord> recordPage = this.page(page, queryWrapper);
        return getRepairRecordVOPage(recordPage);
    }
}
