package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairRatingAddRequest;
import com.rosy.main.domain.dto.repair.RepairRatingQueryRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRating;
import com.rosy.main.domain.vo.RepairRatingVO;
import com.rosy.main.mapper.RepairRatingMapper;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairRatingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RepairRatingServiceImpl extends ServiceImpl<RepairRatingMapper, RepairRating> implements IRepairRatingService {

    @Resource
    private IRepairOrderService repairOrderService;

    @Override
    public RepairRatingVO getRepairRatingVO(RepairRating repairRating) {
        return Optional.ofNullable(repairRating)
                .map(rating -> BeanUtil.copyProperties(rating, RepairRatingVO.class))
                .orElse(null);
    }

    @Override
    public IPage<RepairRatingVO> getRepairRatingVOPage(Page<RepairRating> page) {
        return page.convert(this::getRepairRatingVO);
    }

    @Override
    public LambdaQueryWrapper<RepairRating> getQueryWrapper(RepairRatingQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<RepairRating> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(request.getId() != null, RepairRating::getId, request.getId())
                .eq(request.getOrderId() != null, RepairRating::getOrderId, request.getOrderId())
                .eq(request.getOrderNo() != null, RepairRating::getOrderNo, request.getOrderNo())
                .eq(request.getRating() != null, RepairRating::getRating, request.getRating())
                .eq(request.getCreatorId() != null, RepairRating::getCreatorId, request.getCreatorId())
                .orderByDesc(RepairRating::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRepairRating(RepairRatingAddRequest request) {
        RepairOrder repairOrder = repairOrderService.getById(request.getOrderId());
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        ThrowUtils.throwIf(repairOrder.getStatus() != 3, ErrorCode.OPERATION_ERROR, "只能对已完成的工单进行评价");
        LambdaQueryWrapper<RepairRating> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairRating::getOrderId, request.getOrderId());
        RepairRating existingRating = this.getOne(queryWrapper);
        ThrowUtils.throwIf(existingRating != null, ErrorCode.OPERATION_ERROR, "该工单已评价，不能重复评价");
        RepairRating repairRating = BeanUtil.copyProperties(request, RepairRating.class);
        repairRating.setOrderNo(repairOrder.getOrderNo());
        boolean result = this.save(repairRating);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建评价失败");
        return repairRating.getId();
    }

    @Override
    public RepairRatingVO getRatingByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairRating> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairRating::getOrderId, orderId);
        RepairRating repairRating = this.getOne(queryWrapper);
        return getRepairRatingVO(repairRating);
    }
}
