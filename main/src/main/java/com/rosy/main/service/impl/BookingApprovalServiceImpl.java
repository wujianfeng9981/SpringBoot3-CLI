package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.BookingApprovalRequest;
import com.rosy.main.domain.entity.BookingApproval;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.vo.BookingApprovalVO;
import com.rosy.main.mapper.BookingApprovalMapper;
import com.rosy.main.service.IBookingApprovalService;
import com.rosy.main.service.IMeetingBookingService;
import com.rosy.main.service.IMeetingNotificationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BookingApprovalServiceImpl extends ServiceImpl<BookingApprovalMapper, BookingApproval> implements IBookingApprovalService {

    @Resource
    private IMeetingBookingService meetingBookingService;

    @Resource
    private IMeetingNotificationService notificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processApproval(BookingApprovalRequest request) {
        MeetingBooking booking = meetingBookingService.getById(request.getBookingId());
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        ThrowUtils.throwIf(booking.getStatus() != 0, ErrorCode.OPERATION_ERROR, "该预约已处理");
        BookingApproval approval = new BookingApproval();
        BeanUtil.copyProperties(request, approval);
        approval.setApprovalTime(LocalDateTime.now());
        boolean saved = this.save(approval);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "审批记录保存失败");
        if (request.getAction() == 1) {
            booking.setStatus((byte) 1);
        } else {
            booking.setStatus((byte) 2);
        }
        boolean updated = meetingBookingService.updateById(booking);
        ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "更新预约状态失败");
        String title = request.getAction() == 1 ? "预约审批通过" : "预约审批驳回";
        String content = String.format("您的会议预约[%s]已被%s", booking.getTitle(), request.getAction() == 1 ? "通过" : "驳回");
        notificationService.createNotification(booking.getApplicantId(), booking.getId(), (byte) 1, title, content);
    }

    @Override
    public Page<BookingApprovalVO> listApprovals(int current, int size, Long bookingId, Long approverId, Byte action) {
        Page<BookingApproval> page = new Page<>(current, size);
        LambdaQueryWrapper<BookingApproval> wrapper = new LambdaQueryWrapper<>();
        if (bookingId != null) {
            wrapper.eq(BookingApproval::getBookingId, bookingId);
        }
        if (approverId != null) {
            wrapper.eq(BookingApproval::getApproverId, approverId);
        }
        if (action != null) {
            wrapper.eq(BookingApproval::getAction, action);
        }
        wrapper.orderByDesc(BookingApproval::getApprovalTime);
        Page<BookingApproval> resultPage = this.page(page, wrapper);
        Page<BookingApprovalVO> voPage = new Page<>(current, size, resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(approval -> {
            BookingApprovalVO vo = new BookingApprovalVO();
            BeanUtil.copyProperties(approval, vo);
            return vo;
        }).toList());
        return voPage;
    }
}
