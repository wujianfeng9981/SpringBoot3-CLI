package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalAddRequest;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalQueryRequest;
import com.rosy.main.domain.entity.BookingApproval;
import com.rosy.main.domain.vo.BookingApprovalVO;

/**
 * <p>
 * 预约审批表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface IBookingApprovalService extends IService<BookingApproval> {

    /**
     * 审批预约
     *
     * @param request    审批请求
     * @param approverId 审批人ID
     * @return 是否成功
     */
    boolean approve(BookingApprovalAddRequest request, Long approverId);

    /**
     * 获取审批VO
     *
     * @param bookingApproval 审批实体
     * @return 审批VO
     */
    BookingApprovalVO getApprovalVO(BookingApproval bookingApproval);

    /**
     * 获取审批VO分页
     *
     * @param page 分页对象
     * @return VO分页对象
     */
    Page<BookingApprovalVO> getApprovalVOPage(Page<BookingApproval> page);

    /**
     * 获取查询条件包装器
     *
     * @param queryRequest 查询请求
     * @return 查询条件包装器
     */
    LambdaQueryWrapper<BookingApproval> getQueryWrapper(BookingApprovalQueryRequest queryRequest);
}
