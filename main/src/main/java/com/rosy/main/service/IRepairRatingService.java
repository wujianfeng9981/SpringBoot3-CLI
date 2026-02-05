package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairRatingQueryRequest;
import com.rosy.main.domain.entity.RepairRating;
import com.rosy.main.domain.vo.RepairRatingVO;

/**
 * <p>
 * 维修评价表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface IRepairRatingService extends IService<RepairRating> {

    /**
     * 获取评价VO
     */
    RepairRatingVO getRepairRatingVO(RepairRating repairRating);

    /**
     * 获取评价VO分页
     */
    IPage<RepairRatingVO> getRepairRatingVOPage(Page<RepairRating> page);

    /**
     * 获取查询条件
     */
    LambdaQueryWrapper<RepairRating> getQueryWrapper(RepairRatingQueryRequest queryRequest);

    /**
     * 根据工单ID获取评价
     */
    RepairRatingVO getRatingByOrderId(Long orderId);
}
