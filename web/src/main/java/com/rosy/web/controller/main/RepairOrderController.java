package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairOrderAddRequest;
import com.rosy.main.domain.dto.repair.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.dto.repair.RepairOrderUpdateRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 报修工单表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/repair/order")
public class RepairOrderController {

    @Resource
    private IRepairOrderService repairOrderService;

    // region 增删改查

    /**
     * 创建报修工单
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRepairOrder(@RequestBody RepairOrderAddRequest repairOrderAddRequest) {
        RepairOrder repairOrder = new RepairOrder();
        BeanUtils.copyProperties(repairOrderAddRequest, repairOrder);
        // 生成工单编号
        repairOrder.setOrderNo("RO" + System.currentTimeMillis());
        // 设置初始状态为待处理
        repairOrder.setStatus((byte) 0);
        boolean result = repairOrderService.save(repairOrder);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(repairOrder.getId());
    }

    /**
     * 删除报修工单
     */
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteRepairOrder(@RequestBody IdRequest idRequest) {
        boolean result = repairOrderService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新报修工单
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateRepairOrder(@RequestBody RepairOrderUpdateRequest repairOrderUpdateRequest) {
        if (repairOrderUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder repairOrder = BeanUtil.copyProperties(repairOrderUpdateRequest, RepairOrder.class);
        boolean result = repairOrderService.updateById(repairOrder);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    public ApiResponse getRepairOrderById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder repairOrder = repairOrderService.getById(id);
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(repairOrder);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public ApiResponse getRepairOrderVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder repairOrder = repairOrderService.getById(id);
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR);
        RepairOrderVO vo = repairOrderService.getRepairOrderVO(repairOrder);
        return ApiResponse.success(vo);
    }

    /**
     * 分页获取列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listRepairOrderByPage(@RequestBody RepairOrderQueryRequest repairOrderQueryRequest) {
        long current = repairOrderQueryRequest.getCurrent();
        long size = repairOrderQueryRequest.getPageSize();
        Page<RepairOrder> page = repairOrderService.page(new Page<>(current, size),
                repairOrderService.getQueryWrapper(repairOrderQueryRequest));
        var voPage = repairOrderService.getRepairOrderVOPage(page);
        return ApiResponse.success(voPage);
    }

    // endregion

    // region 工单操作

    /**
     * 分配工单
     */
    @PostMapping("/assign")
    @ValidateRequest
    public ApiResponse assignOrder(@RequestBody RepairOrderAssignRequest assignRequest) {
        boolean result = repairOrderService.assignOrder(assignRequest.getOrderId(), 
                assignRequest.getRepairmanId(), assignRequest.getAssignType());
        return ApiResponse.success(result);
    }

    /**
     * 接单
     */
    @PostMapping("/accept")
    public ApiResponse acceptOrder(@RequestParam Long orderId, @RequestParam Long repairmanId) {
        if (orderId == null || repairmanId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = repairOrderService.acceptOrder(orderId, repairmanId);
        return ApiResponse.success(result);
    }

    /**
     * 开始维修
     */
    @PostMapping("/start")
    public ApiResponse startRepair(@RequestParam Long orderId) {
        if (orderId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = repairOrderService.startRepair(orderId);
        return ApiResponse.success(result);
    }

    /**
     * 完成维修
     */
    @PostMapping("/complete")
    public ApiResponse completeOrder(@RequestParam Long orderId) {
        if (orderId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = repairOrderService.completeOrder(orderId);
        return ApiResponse.success(result);
    }

    // endregion
}
