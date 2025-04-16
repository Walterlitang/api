package com.app.vo;

import com.app.pojo.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginVo implements Serializable {
    private List<User> userList;
    private User user;
    private List<UserLoginVo> userLoginVos;
    private String token;
    private Integer code;
    private String unionId;
}
