package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairRatingAddRequest;
import com.rosy.main.domain.dto.repair.RepairRatingQueryRequest;
import com.rosy.main.domain.entity.RepairRating;
import com.rosy.main.domain.vo.RepairRatingVO;

public interface IRepairRatingService extends IService<RepairRating> {

    RepairRatingVO getRepairRatingVO(RepairRating repairRating);

    IPage<RepairRatingVO> getRepairRatingVOPage(Page<RepairRating> page);

    LambdaQueryWrapper<RepairRating> getQueryWrapper(RepairRatingQueryRequest request);

    Long createRepairRating(RepairRatingAddRequest request);

    RepairRatingVO getRatingByOrderId(Long orderId);
}
