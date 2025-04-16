package com.app.ResponseVo;

import lombok.Data;

import java.util.List;

@Data
public class CategoryCountResponse {

    private List<CategoryCountItem> items;

    @Data
    public static class CategoryCountItem {
        private Integer id;
        private String name;
        private int count;
    }
}
