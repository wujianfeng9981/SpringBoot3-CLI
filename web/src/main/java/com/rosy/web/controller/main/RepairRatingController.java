package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
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

@RestController
@RequestMapping("/repair/rating")
public class RepairRatingController {

    @Resource
    private IRepairRatingService repairRatingService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRepairRating(@RequestBody RepairRatingAddRequest request) {
        Long ratingId = repairRatingService.createRepairRating(request);
        return ApiResponse.success(ratingId);
    }

    @GetMapping("/get")
    public ApiResponse getRepairRatingById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRating repairRating = repairRatingService.getById(id);
        ThrowUtils.throwIf(repairRating == null, ErrorCode.NOT_FOUND_ERROR, "评价不存在");
        return ApiResponse.success(repairRating);
    }

    @GetMapping("/get/vo")
    public ApiResponse getRepairRatingVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRating repairRating = repairRatingService.getById(id);
        ThrowUtils.throwIf(repairRating == null, ErrorCode.NOT_FOUND_ERROR, "评价不存在");
        return ApiResponse.success(repairRatingService.getRepairRatingVO(repairRating));
    }

    @GetMapping("/get/by-order")
    public ApiResponse getRatingByOrderId(@RequestParam Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRatingVO vo = repairRatingService.getRatingByOrderId(orderId);
        return ApiResponse.success(vo);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listRepairRatingByPage(@RequestBody RepairRatingQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<RepairRating> page = repairRatingService.page(new Page<>(current, size),
                repairRatingService.getQueryWrapper(request));
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listRepairRatingVOByPage(@RequestBody RepairRatingQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        ThrowUtils.throwIf(size > 100, ErrorCode.PARAMS_ERROR, "每页条数不能超过100");
        Page<RepairRating> page = repairRatingService.page(new Page<>(current, size),
                repairRatingService.getQueryWrapper(request));
        var voPage = repairRatingService.getRepairRatingVOPage(page);
        return ApiResponse.success(voPage);
    }
}
