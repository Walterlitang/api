package com.app.vo;

import com.app.pojo.User;
import lombok.Data;

import java.util.List;


@Data
public class UserVo {
    private Integer count;
    private List<User> list;
}
