package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairRecordQueryRequest;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;

/**
 * <p>
 * 维修记录表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface IRepairRecordService extends IService<RepairRecord> {

    /**
     * 获取维修记录VO
     */
    RepairRecordVO getRepairRecordVO(RepairRecord repairRecord);

    /**
     * 获取维修记录VO分页
     */
    IPage<RepairRecordVO> getRepairRecordVOPage(Page<RepairRecord> page);

    /**
     * 获取查询条件
     */
    LambdaQueryWrapper<RepairRecord> getQueryWrapper(RepairRecordQueryRequest queryRequest);

    /**
     * 根据工单ID获取维修记录
     */
    IPage<RepairRecordVO> getRecordsByOrderId(Long orderId, long current, long size);
}
