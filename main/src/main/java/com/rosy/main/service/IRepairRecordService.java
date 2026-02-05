package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.dto.repair.RepairRecordQueryRequest;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;

public interface IRepairRecordService extends IService<RepairRecord> {

    RepairRecordVO getRepairRecordVO(RepairRecord repairRecord);

    IPage<RepairRecordVO> getRepairRecordVOPage(Page<RepairRecord> page);

    LambdaQueryWrapper<RepairRecord> getQueryWrapper(RepairRecordQueryRequest request);

    Long createRepairRecord(RepairRecordAddRequest request);

    IPage<RepairRecordVO> getRecordsByOrderId(Long orderId, long current, long size);
}
