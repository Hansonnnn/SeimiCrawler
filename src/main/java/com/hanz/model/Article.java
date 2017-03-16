package com.hanz.model;

import cn.wanghaomiao.seimi.annotation.Xpath;
import lombok.Data;

/**
 * Created by smith on 2017/3/14.
 */
@Data public class Article {


    private int id;

    private String url;

    @Xpath("//div[@class='title']/text()")
    private String title;

    @Xpath("//div[@class='blog-body']/textarea/text()")
    private String Content;


}
