package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalAddRequest;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalQueryRequest;
import com.rosy.main.domain.entity.BookingApproval;
import com.rosy.main.domain.vo.BookingApprovalVO;

public interface IBookingApprovalService extends IService<BookingApproval> {

    void approve(BookingApprovalAddRequest request, Long approverId);

    BookingApprovalVO getApprovalVO(BookingApproval approval);

    Page<BookingApprovalVO> getApprovalVOPage(Page<BookingApproval> page);

    LambdaQueryWrapper<BookingApproval> getQueryWrapper(BookingApprovalQueryRequest queryRequest);
}
