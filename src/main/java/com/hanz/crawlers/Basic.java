package com.hanz.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import cn.wanghaomiao.xpath.model.JXDocument;

import java.util.List;

/**
 * Created by smith on 2017/3/13.
 */
@Crawler(name = "basic")
public class Basic extends BaseSeimiCrawler {

    //@Override
    public String[] startUrls() {
        return new String[]{"https://www.oschina.net/blog/"};
    }
    //@Override
    public void start(Response response) {
        JXDocument doc = response.document();
        try {
            String xPath="//div[@id='topsOfRecommend']/div[@class='box item']/div[@class='box-aw']/header/a/@href";
            int pages=50;
            for(int i=0;i<pages;i++){

            }
            List<Object> urls = doc.sel(xPath);
            logger.info("{}", urls.size());
            for (Object s:urls){
                System.out.println(s);
                push(new Request(s.toString(),"getTitle"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getTitle(Response response){
        JXDocument doc = response.document();
        try {

            logger.info("title:{} {} {} ", response.getUrl(), doc.sel("//div[@class='title']/text()"),doc.sel("//div[@class='blog-body']/textarea/text()"));
            //do something
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getContent(Response response){
        JXDocument doc=response.document();
        try{
            logger.info("url:{} {}", response.getUrl(), doc.sel("//h1[@class='postTitle']/a/text()|//a[@id='cb_post_title_url']/text()"));

        } catch (Exception e){

            e.printStackTrace();
        }
    }
}