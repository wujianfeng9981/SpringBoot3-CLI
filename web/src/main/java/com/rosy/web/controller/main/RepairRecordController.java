package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
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

@RestController
@RequestMapping("/repair/record")
public class RepairRecordController {

    @Resource
    private IRepairRecordService repairRecordService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRepairRecord(@RequestBody RepairRecordAddRequest request) {
        Long recordId = repairRecordService.createRepairRecord(request);
        return ApiResponse.success(recordId);
    }

    @GetMapping("/get")
    public ApiResponse getRepairRecordById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRecord repairRecord = repairRecordService.getById(id);
        ThrowUtils.throwIf(repairRecord == null, ErrorCode.NOT_FOUND_ERROR, "记录不存在");
        return ApiResponse.success(repairRecord);
    }

    @GetMapping("/get/vo")
    public ApiResponse getRepairRecordVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRecord repairRecord = repairRecordService.getById(id);
        ThrowUtils.throwIf(repairRecord == null, ErrorCode.NOT_FOUND_ERROR, "记录不存在");
        return ApiResponse.success(repairRecordService.getRepairRecordVO(repairRecord));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listRepairRecordByPage(@RequestBody RepairRecordQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<RepairRecord> page = repairRecordService.page(new Page<>(current, size),
                repairRecordService.getQueryWrapper(request));
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listRepairRecordVOByPage(@RequestBody RepairRecordQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        ThrowUtils.throwIf(size > 100, ErrorCode.PARAMS_ERROR, "每页条数不能超过100");
        Page<RepairRecord> page = repairRecordService.page(new Page<>(current, size),
                repairRecordService.getQueryWrapper(request));
        var voPage = repairRecordService.getRepairRecordVOPage(page);
        return ApiResponse.success(voPage);
    }

    @GetMapping("/list/by-order")
    public ApiResponse getRecordsByOrderId(@RequestParam Long orderId,
                                           @RequestParam(defaultValue = "1") long current,
                                           @RequestParam(defaultValue = "20") long size) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        var page = repairRecordService.getRecordsByOrderId(orderId, current, size);
        return ApiResponse.success(page);
    }
}
