package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.signin.SignInQueryRequest;
import com.rosy.main.domain.entity.Reservation;
import com.rosy.main.domain.entity.SignIn;
import com.rosy.main.domain.enums.SignInStatus;
import com.rosy.main.domain.vo.SignInVO;
import com.rosy.main.mapper.SignInMapper;
import com.rosy.main.service.IReservationService;
import com.rosy.main.service.ISignInService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SignInServiceImpl extends ServiceImpl<SignInMapper, SignIn> implements ISignInService {

    @Resource
    private IReservationService reservationService;

    @Override
    public SignInVO getSignInVO(SignIn signIn) {
        return Optional.ofNullable(signIn)
                .map(s -> {
                    SignInVO vo = BeanUtil.copyProperties(s, SignInVO.class);
                    Reservation reservation = reservationService.getById(s.getReservationId());
                    if (reservation != null) {
                        vo.setMeetingTitle(reservation.getTitle());
                    }
                    SignInStatus status = SignInStatus.getByCode(s.getSignInStatus());
                    if (status != null) {
                        vo.setSignInStatusDesc(status.getDesc());
                    }
                    return vo;
                })
                .orElse(null);
    }

    @Override
    public LambdaQueryWrapper<SignIn> getQueryWrapper(SignInQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<SignIn> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getReservationId(), SignIn::getReservationId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), SignIn::getUserId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getSignInStatus(), SignIn::getSignInStatus);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                SignIn::getId);

        return queryWrapper;
    }

    @Override
    public int countSignedByReservationId(Long reservationId) {
        return baseMapper.countSignedByReservationId(reservationId);
    }

    @Override
    public int countTotalByReservationId(Long reservationId) {
        return baseMapper.countTotalByReservationId(reservationId);
    }
}
