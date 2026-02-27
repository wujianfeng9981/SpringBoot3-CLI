package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.signin.SignInQueryRequest;
import com.rosy.main.domain.entity.SignIn;
import com.rosy.main.domain.vo.SignInVO;

public interface ISignInService extends IService<SignIn> {

    SignInVO getSignInVO(SignIn signIn);

    LambdaQueryWrapper<SignIn> getQueryWrapper(SignInQueryRequest queryRequest);

    int countSignedByReservationId(Long reservationId);

    int countTotalByReservationId(Long reservationId);
}
