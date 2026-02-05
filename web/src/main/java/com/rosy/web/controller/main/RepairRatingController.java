package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairRatingAddRequest;
import com.rosy.main.domain.dto.repair.RepairRatingQueryRequest;
import com.rosy.main.domain.entity.RepairRating;
import com.rosy.main.domain.vo.RepairRatingVO;
import com.rosy.main.service.IRepairRatingService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 维修评价表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/repair/rating")
public class RepairRatingController {

    @Resource
    private IRepairRatingService repairRatingService;

    // region 增删改查

    /**
     * 创建评价
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRepairRating(@RequestBody RepairRatingAddRequest repairRatingAddRequest) {
        RepairRating repairRating = new RepairRating();
        BeanUtil.copyProperties(repairRatingAddRequest, repairRating);
        boolean result = repairRatingService.save(repairRating);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(repairRating.getId());
    }

    /**
     * 删除评价
     */
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteRepairRating(@RequestBody IdRequest idRequest) {
        boolean result = repairRatingService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    public ApiResponse getRepairRatingById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRating repairRating = repairRatingService.getById(id);
        ThrowUtils.throwIf(repairRating == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(repairRating);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public ApiResponse getRepairRatingVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRating repairRating = repairRatingService.getById(id);
        ThrowUtils.throwIf(repairRating == null, ErrorCode.NOT_FOUND_ERROR);
        RepairRatingVO vo = repairRatingService.getRepairRatingVO(repairRating);
        return ApiResponse.success(vo);
    }

    /**
     * 分页获取列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listRepairRatingByPage(@RequestBody RepairRatingQueryRequest repairRatingQueryRequest) {
        long current = repairRatingQueryRequest.getCurrent();
        long size = repairRatingQueryRequest.getPageSize();
        Page<RepairRating> page = repairRatingService.page(new Page<>(current, size),
                repairRatingService.getQueryWrapper(repairRatingQueryRequest));
        var voPage = repairRatingService.getRepairRatingVOPage(page);
        return ApiResponse.success(voPage);
    }

    // endregion

    // region 根据工单查询

    /**
     * 根据工单ID获取评价
     */
    @GetMapping("/get/byOrder")
    public ApiResponse getRatingByOrderId(@RequestParam Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRatingVO vo = repairRatingService.getRatingByOrderId(orderId);
        return ApiResponse.success(vo);
    }

    // endregion
}
