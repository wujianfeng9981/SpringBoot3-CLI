package com.rosy.main.domain.dto.signin;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SignInRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "预约ID不能为空")
    private Long reservationId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Size(max = 200, message = "备注长度不能超过200个字符")
    private String remark;
}
