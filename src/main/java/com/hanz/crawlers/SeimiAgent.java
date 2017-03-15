package com.hanz.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.core.SeimiCrawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.http.SeimiAgentContentType;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import cn.wanghaomiao.xpath.model.JXDocument;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smith on 2017/3/14.
 *
 */
@Crawler(name="seimiagent")
public class SeimiAgent extends BaseSeimiCrawler {

   // @Value("${seimiAgentHost}")
    private String seimiAgentHost="192.168.0.122";

    //@Value("${seimiAgentPort}")
    private int seimiAgentPort=8000;

    //@Override
    public String[] startUrls() {
        return new String[]{"https://www.oschina.net/blog/"};
    }

    @Override
    public String seimiAgentHost() {
        return this.seimiAgentHost;
    }

    @Override
    public int seimiAgentPort() {
        return this.seimiAgentPort;
    }


    public void start(Response response) {
        JXDocument doc = response.document();
        List<Object> urls=new ArrayList<Object>();
        String xPath="//div[@id='topsOfRecommend']/div[@class='box item']/div[@class='box-aw']/header/a/@href";
        try {
            urls.addAll(doc.sel(xPath));
            int pageNum=50;
            for (int i = 2; i < pageNum; i++) {
                Request seimiAgentReq = Request.build("https://www.oschina.net/action/ajax/get_more_recommend_blog?classification=0&p="+i, "getTitle")
                        .useSeimiAgent()
                        //设置全部load完成后给SeimiAgent多少时间用于执行js并渲染页面，单位为毫秒
                        .setSeimiAgentRenderTime(5000);
                push(seimiAgentReq);
            }
        }catch(Exception e){
            logger.error("error{}",e.getMessage());
        }

    }

    public void getTitle(Response response){
        JXDocument doc = response.document();
        String xPath="//header/a/@href";
        try {
            List<Object> urls = doc.sel(xPath);
            logger.info("urls {}", urls.size());
            for(Object s:urls){
                System.out.println(s);
            }
            //do something
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
