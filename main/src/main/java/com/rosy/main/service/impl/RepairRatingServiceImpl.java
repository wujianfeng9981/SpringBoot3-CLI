package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.repair.RepairRatingQueryRequest;
import com.rosy.main.domain.entity.RepairRating;
import com.rosy.main.domain.vo.RepairRatingVO;
import com.rosy.main.mapper.RepairRatingMapper;
import com.rosy.main.service.IRepairRatingService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 维修评价表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class RepairRatingServiceImpl extends ServiceImpl<RepairRatingMapper, RepairRating> implements IRepairRatingService {

    @Override
    public RepairRatingVO getRepairRatingVO(RepairRating repairRating) {
        if (repairRating == null) {
            return null;
        }
        return BeanUtil.copyProperties(repairRating, RepairRatingVO.class);
    }

    @Override
    public IPage<RepairRatingVO> getRepairRatingVOPage(Page<RepairRating> page) {
        return page.convert(this::getRepairRatingVO);
    }

    @Override
    public LambdaQueryWrapper<RepairRating> getQueryWrapper(RepairRatingQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<RepairRating> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getOrderId(), RepairRating::getOrderId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), RepairRating::getUserId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getRepairmanId(), RepairRating::getRepairmanId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getRating(), RepairRating::getRating);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                RepairRating::getId);

        return queryWrapper;
    }

    @Override
    public RepairRatingVO getRatingByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairRating> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairRating::getOrderId, orderId);
        RepairRating rating = getOne(queryWrapper);
        return getRepairRatingVO(rating);
    }
}
