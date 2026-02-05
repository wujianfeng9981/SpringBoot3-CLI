package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.repair.RepairRecordQueryRequest;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;
import com.rosy.main.mapper.RepairRecordMapper;
import com.rosy.main.service.IRepairRecordService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 维修记录表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class RepairRecordServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements IRepairRecordService {

    @Override
    public RepairRecordVO getRepairRecordVO(RepairRecord repairRecord) {
        if (repairRecord == null) {
            return null;
        }
        RepairRecordVO vo = BeanUtil.copyProperties(repairRecord, RepairRecordVO.class);
        vo.setActionTypeName(getActionTypeName(repairRecord.getActionType()));
        return vo;
    }

    @Override
    public IPage<RepairRecordVO> getRepairRecordVOPage(Page<RepairRecord> page) {
        return page.convert(this::getRepairRecordVO);
    }

    @Override
    public LambdaQueryWrapper<RepairRecord> getQueryWrapper(RepairRecordQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<RepairRecord> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getOrderId(), RepairRecord::getOrderId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getRepairmanId(), RepairRecord::getRepairmanId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getActionType(), RepairRecord::getActionType);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                RepairRecord::getId);

        return queryWrapper;
    }

    @Override
    public IPage<RepairRecordVO> getRecordsByOrderId(Long orderId, long current, long size) {
        Page<RepairRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<RepairRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairRecord::getOrderId, orderId);
        queryWrapper.orderByDesc(RepairRecord::getCreateTime);
        return getRepairRecordVOPage(page(page, queryWrapper));
    }

    private String getActionTypeName(Byte actionType) {
        if (actionType == null) return "";
        return switch (actionType) {
            case 1 -> "接单";
            case 2 -> "开始维修";
            case 3 -> "完成维修";
            case 4 -> "转派";
            case 5 -> "备注";
            default -> "";
        };
    }
}
