package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SignInVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long reservationId;

    private String meetingTitle;

    private Long userId;

    private String userName;

    private Byte signInStatus;

    private String signInStatusDesc;

    private LocalDateTime signInTime;

    private String remark;

    private LocalDateTime createTime;
}
