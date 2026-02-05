package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.constant.CommonConstant;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.SqlUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalAddRequest;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalQueryRequest;
import com.rosy.main.domain.entity.BookingApproval;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.vo.BookingApprovalVO;
import com.rosy.main.mapper.BookingApprovalMapper;
import com.rosy.main.mapper.MeetingBookingMapper;
import com.rosy.main.service.IBookingApprovalService;
import com.rosy.main.service.IMeetingNotificationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 预约审批表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class BookingApprovalServiceImpl extends ServiceImpl<BookingApprovalMapper, BookingApproval> implements IBookingApprovalService {

    @Resource
    private MeetingBookingMapper meetingBookingMapper;

    @Resource
    private IMeetingNotificationService meetingNotificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approve(BookingApprovalAddRequest request, Long approverId) {
        // 查询预约
        MeetingBooking booking = meetingBookingMapper.selectById(request.getBookingId());
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR, "预约不存在");

        // 只有待审批状态可以审批
        ThrowUtils.throwIf(booking.getStatus() != 0, ErrorCode.OPERATION_ERROR, "该预约已审批");

        // 更新预约状态
        booking.setStatus(request.getStatus()); // 1-通过，2-驳回
        boolean result = meetingBookingMapper.updateById(booking) > 0;
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

        // 保存审批记录
        BookingApproval approval = new BookingApproval();
        approval.setBookingId(request.getBookingId());
        approval.setApproverId(approverId);
        approval.setStatus(request.getStatus());
        approval.setComment(request.getComment());
        result = this.save(approval);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

        // 发送通知
        meetingNotificationService.sendApprovalNotification(
                request.getBookingId(),
                booking.getUserId(),
                request.getStatus(),
                request.getComment()
        );

        return true;
    }

    @Override
    public BookingApprovalVO getApprovalVO(BookingApproval approval) {
        if (approval == null) {
            return null;
        }
        BookingApprovalVO approvalVO = new BookingApprovalVO();
        BeanUtil.copyProperties(approval, approvalVO);

        // 查询预约信息
        MeetingBooking booking = meetingBookingMapper.selectById(approval.getBookingId());
        if (booking != null) {
            approvalVO.setBookingTitle(booking.getTitle());
        }

        return approvalVO;
    }

    @Override
    public Page<BookingApprovalVO> getApprovalVOPage(Page<BookingApproval> page) {
        List<BookingApproval> records = page.getRecords();
        Page<BookingApprovalVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<BookingApprovalVO> voList = records.stream()
                .map(this::getApprovalVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<BookingApproval> getQueryWrapper(BookingApprovalQueryRequest queryRequest) {
        LambdaQueryWrapper<BookingApproval> queryWrapper = new LambdaQueryWrapper<>();
        if (queryRequest == null) {
            return queryWrapper;
        }

        Long id = queryRequest.getId();
        Long bookingId = queryRequest.getBookingId();
        Long approverId = queryRequest.getApproverId();
        Byte status = queryRequest.getStatus();
        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();

        queryWrapper.eq(id != null, BookingApproval::getId, id);
        queryWrapper.eq(bookingId != null, BookingApproval::getBookingId, bookingId);
        queryWrapper.eq(approverId != null, BookingApproval::getApproverId, approverId);
        queryWrapper.eq(status != null, BookingApproval::getStatus, status);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder),
                BookingApproval::getCreateTime);
        return queryWrapper;
    }
}
