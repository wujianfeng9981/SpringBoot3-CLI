package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.dto.repair.RepairRecordQueryRequest;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;
import com.rosy.main.service.IRepairRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 维修记录表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/repair/record")
public class RepairRecordController {

    @Resource
    private IRepairRecordService repairRecordService;

    // region 增删改查

    /**
     * 创建维修记录
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRepairRecord(@RequestBody RepairRecordAddRequest repairRecordAddRequest) {
        RepairRecord repairRecord = new RepairRecord();
        BeanUtil.copyProperties(repairRecordAddRequest, repairRecord);
        boolean result = repairRecordService.save(repairRecord);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(repairRecord.getId());
    }

    /**
     * 删除维修记录
     */
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteRepairRecord(@RequestBody IdRequest idRequest) {
        boolean result = repairRecordService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    public ApiResponse getRepairRecordById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRecord repairRecord = repairRecordService.getById(id);
        ThrowUtils.throwIf(repairRecord == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(repairRecord);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public ApiResponse getRepairRecordVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRecord repairRecord = repairRecordService.getById(id);
        ThrowUtils.throwIf(repairRecord == null, ErrorCode.NOT_FOUND_ERROR);
        RepairRecordVO vo = repairRecordService.getRepairRecordVO(repairRecord);
        return ApiResponse.success(vo);
    }

    /**
     * 分页获取列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listRepairRecordByPage(@RequestBody RepairRecordQueryRequest repairRecordQueryRequest) {
        long current = repairRecordQueryRequest.getCurrent();
        long size = repairRecordQueryRequest.getPageSize();
        Page<RepairRecord> page = repairRecordService.page(new Page<>(current, size),
                repairRecordService.getQueryWrapper(repairRecordQueryRequest));
        var voPage = repairRecordService.getRepairRecordVOPage(page);
        return ApiResponse.success(voPage);
    }

    // endregion

    // region 根据工单查询

    /**
     * 根据工单ID获取维修记录列表
     */
    @GetMapping("/list/byOrder")
    public ApiResponse getRecordsByOrderId(@RequestParam Long orderId,
                                           @RequestParam(defaultValue = "1") long current,
                                           @RequestParam(defaultValue = "10") long size) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        var voPage = repairRecordService.getRecordsByOrderId(orderId, current, size);
        return ApiResponse.success(voPage);
    }

    // endregion
}
