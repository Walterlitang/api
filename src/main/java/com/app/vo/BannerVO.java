package com.app.vo;

import com.app.model.Banner;
import lombok.Data;

import java.util.List;

@Data
public class BannerVO {
    private List<Banner> topBanners;
    private List<Banner> bottomBanners;
}
