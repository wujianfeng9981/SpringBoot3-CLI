package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.BookingApprovalRequest;
import com.rosy.main.domain.entity.BookingApproval;
import com.rosy.main.domain.vo.BookingApprovalVO;

public interface IBookingApprovalService extends IService<BookingApproval> {

    void processApproval(BookingApprovalRequest request);

    Page<BookingApprovalVO> listApprovals(int current, int size, Long bookingId, Long approverId, Byte action);
}
