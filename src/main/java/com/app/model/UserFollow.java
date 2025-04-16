package com.app.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户关注表
 * </p>
 *
 * @author yoominic
 * @since 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lm_user_follow")
public class UserFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关系ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关注者ID
     */
    private Integer followerId;

    /**
     * 被关注者ID
     */
    private Integer followedId;

    /**
     * 关注时间
     */
    private LocalDateTime createdAt;


}
