package com.app.common;

import com.app.ResponseVo.ArticleResponse;
import com.app.model.Article;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.function.BiConsumer;

public enum ArticleCategory {
    FOOT(ArticleConstants.FootID, ArticleResponse::setFootPages),
    NEWS(ArticleConstants.NewsID, ArticleResponse::setNewsPages),
    ROCKET(ArticleConstants.RocketID, ArticleResponse::setRocketPages),
    STUDY(ArticleConstants.StudyID, ArticleResponse::setStudyPages),
    THEORY(ArticleConstants.TheoryID, ArticleResponse::setTheoryPages),
    COURSE(ArticleConstants.CourseID, ArticleResponse::setCoursePages),
    LI_JIAN_HEI_XI(ArticleConstants.LiJianHeiXiID, ArticleResponse::setLiJianHeiXiPages),
    DONG_FENG_LUN_JIAN(ArticleConstants.DongFengLunJianID, ArticleResponse::setDongFengLunJianPages),
    JUN_QING_ZONG_HENG(ArticleConstants.JunQingZongHengID, ArticleResponse::setJunQingZongHengPages),
    JUN_SHI_GONG_ZUO(ArticleConstants.JunShiGongZuoID, ArticleResponse::setJunShiGongZuoPages),
    ZHENG_ZHI_GONG_ZUO(ArticleConstants.ZhengZhiGongZuoID, ArticleResponse::setZhengZhiGongZuoPages),
    BAO_ZHANG_GONG_ZUO(ArticleConstants.BaoZhangGongZuoID, ArticleResponse::setBaoZhangGongZuoPages),
    LV_SHI_ZAI_XIAN(ArticleConstants.LvShiZaiXianID, ArticleResponse::setLvShiZaiXianPages),
    XIN_LING_YI_ZHAN(ArticleConstants.XinLingYiZhanID, ArticleResponse::setXinLingYiZhanPages),
    JIAN_KANG_WEN_ZHEN(ArticleConstants.JianKangWenZhenID, ArticleResponse::setJianKangWenZhenPages);

    private final int categoryId;
    private final BiConsumer<ArticleResponse, IPage<Article>> setterMethod;

    ArticleCategory(int categoryId, BiConsumer<ArticleResponse, IPage<Article>> setterMethod) {
        this.categoryId = categoryId;
        this.setterMethod = setterMethod;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public BiConsumer<ArticleResponse, IPage<Article>> getSetterMethod() {
        return setterMethod;
    }

    public static ArticleCategory fromCategoryId(int categoryId) {
        for (ArticleCategory category : values()) {
            if (category.getCategoryId() == categoryId) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category ID: " + categoryId);
    }
}
