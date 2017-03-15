package com.hanz.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.core.SeimiCrawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.http.HttpMethod;
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

    @Value("${seimiAgentHost}")
    private String seimiAgentHost;

    @Value("${seimiAgentPort}")
    private int seimiAgentPort;

    private List<Object>urls=new ArrayList<Object>();

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
        List<Request> seimiRequests=new ArrayList<Request>();
        String xPath="//div[@id='topsOfRecommend']/div[@class='box item']/div[@class='box-aw']/header/a/@href";
        try {
            urls.addAll(doc.sel(xPath));
            int pageNum=50;
            for (int i = 2; i < pageNum; i++) {
                Request seimiAgentReq = Request.build("https://www.oschina.net/action/ajax/get_more_recommend_blog?classification=0&p="+i
                        , "getTitle")
                        .useSeimiAgent()
                        .setUrl("https://www.oschina.net/action/ajax/get_more_recommend_blog?classification=0&p="+i)
                        //.setCallBack("getTitle")
                        .setHttpMethod(HttpMethod.POST)
                        .setSeimiAgentRenderTime(3000);
                seimiRequests.add(seimiAgentReq);
            }
            for(Request seimi:seimiRequests){
                push(seimi);
            }
        }catch(Exception e){
            logger.error("error{}",e.getMessage());
        }

    }


    public void getContent(Response response){
        JXDocument doc = response.document();
        try {
            logger.info("title:{} {} {} ", response.getUrl(), doc.sel("//div[@class='title']/text()"), doc.sel("//div[@class='blog-body']/textarea/text()"));
        }catch(Exception e){
            logger.error("error title:{}",e.getMessage());
        }
    }


    public void getTitle(Response response){
        JXDocument doc = response.document();
        String xPath="//header/a/@href";
        try {
            urls.addAll(doc.sel(xPath));
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
