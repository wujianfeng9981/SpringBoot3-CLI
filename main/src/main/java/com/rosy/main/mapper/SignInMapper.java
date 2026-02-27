package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.SignIn;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SignInMapper extends BaseMapper<SignIn> {

    @Select("SELECT COUNT(*) FROM sign_in WHERE reservation_id = #{reservationId} AND sign_in_status = 1 AND is_deleted = 0")
    int countSignedByReservationId(@Param("reservationId") Long reservationId);

    @Select("SELECT COUNT(*) FROM sign_in WHERE reservation_id = #{reservationId} AND is_deleted = 0")
    int countTotalByReservationId(@Param("reservationId") Long reservationId);
}
