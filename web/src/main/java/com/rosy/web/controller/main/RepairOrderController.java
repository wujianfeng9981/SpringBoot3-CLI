package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/order")
public class RepairOrderController {

    @Resource
    private IRepairOrderService repairOrderService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRepairOrder(@RequestBody RepairOrderAddRequest request) {
        Long orderId = repairOrderService.createRepairOrder(request);
        return ApiResponse.success(orderId);
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteRepairOrder(@RequestBody IdRequest idRequest) {
        boolean result = repairOrderService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateRepairOrder(@RequestBody RepairOrderUpdateRequest request) {
        Boolean result = repairOrderService.updateRepairOrder(request);
        return ApiResponse.success(result);
    }

    @GetMapping("/get")
    public ApiResponse getRepairOrderById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder repairOrder = repairOrderService.getById(id);
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        return ApiResponse.success(repairOrder);
    }

    @GetMapping("/get/vo")
    public ApiResponse getRepairOrderVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder repairOrder = repairOrderService.getById(id);
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        return ApiResponse.success(repairOrderService.getRepairOrderVO(repairOrder));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listRepairOrderByPage(@RequestBody RepairOrderQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<RepairOrder> page = repairOrderService.page(new Page<>(current, size),
                repairOrderService.getQueryWrapper(request));
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listRepairOrderVOByPage(@RequestBody RepairOrderQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        ThrowUtils.throwIf(size > 50, ErrorCode.PARAMS_ERROR, "每页条数不能超过50");
        Page<RepairOrder> page = repairOrderService.page(new Page<>(current, size),
                repairOrderService.getQueryWrapper(request));
        Page<RepairOrderVO> voPage = PageUtils.convert(page, repairOrderService::getRepairOrderVO);
        return ApiResponse.success(voPage);
    }

    @PostMapping("/assign")
    @ValidateRequest
    public ApiResponse assignRepairOrder(@RequestBody RepairOrderAssignRequest request) {
        Boolean result = repairOrderService.assignRepairOrder(request);
        return ApiResponse.success(result);
    }

    @PostMapping("/accept")
    @ValidateRequest
    public ApiResponse acceptRepairOrder(@RequestBody IdRequest idRequest) {
        Boolean result = repairOrderService.acceptRepairOrder(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/complete")
    @ValidateRequest
    public ApiResponse completeRepairOrder(@RequestBody IdRequest idRequest) {
        Boolean result = repairOrderService.completeRepairOrder(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/cancel")
    @ValidateRequest
    public ApiResponse cancelRepairOrder(@RequestBody IdRequest idRequest) {
        Boolean result = repairOrderService.cancelRepairOrder(idRequest.getId());
        return ApiResponse.success(result);
    }
}
