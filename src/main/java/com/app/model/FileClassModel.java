package com.app.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lm_file_class")
@JsonInclude(JsonInclude.Include.NON_NULL)

public class FileClassModel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;//分类名称
    private Integer pid;//父级 ID

    @TableField(exist = false)
    private List<FileClassModel> child;// 子集
}
