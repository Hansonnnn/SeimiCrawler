package com.hanz.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import cn.wanghaomiao.xpath.model.JXDocument;
import com.hanz.dao.CrawlerDao;
import com.hanz.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smith on 2017/3/14.
 */
@Crawler(name = "seimiagent")
public class SeimiAgent extends BaseSeimiCrawler {

    @Value("${seimiAgentHost}")
    private String seimiAgentHost;

    @Value("${seimiAgentPort}")
    private int seimiAgentPort;

    @Autowired
    private CrawlerDao crawlerDao;

    private List<Object> urls = new ArrayList<Object>();

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
        String xPath = "//div[@id='topsOfRecommend']/div[@class='box item']/div[@class='box-aw']/header/a/@href";
        try {
            urls.addAll(doc.sel(xPath));
            int pageNum = 50;
            for (int i = 2; i < pageNum; i++) {
                Request seimiAgentReq = Request.build("https://www.oschina.net/action/ajax/get_more_recommend_blog?classification=0&p=" + i
                        , "getTitle");
                push(seimiAgentReq);
            }

        } catch (Exception e) {
            logger.error("error{}", e.getMessage());
        }

    }

    /**
     * 获取文章标题与文章内容
     *
     * @param response
     */

    public void getContent(Response response) {
        JXDocument doc = response.document();
        try {
            logger.info("title:{} {} {} ", response.getUrl(), doc.sel("//div[@class='title']/text()"), doc.sel("//div[@class='blog-body']/textarea/text()"));

            String title = doc.sel("//div[@class='title']/text()").toString();
            String content = doc.sel("//div[@class='blog-body']/textarea/text()").toString();

            Article article = new Article();
            article.setContent(content);
            article.setTitle(title);
            article.setUrl(response.getUrl());

            crawlerDao.save(article);

        } catch (Exception e) {
            logger.error("error title:{}", e.getMessage());
        }
    }

    /**
     * 获取动态渲染页面url
     *
     * @param response
     */

    public void getTitle(Response response) {
        JXDocument doc = response.document();
        String xPath = "//header/a/@href";
        try {
            urls.addAll(doc.sel(xPath));
            logger.info("urls {}", urls.size());

            for (Object s : urls) {
                Request req = Request.build(s.toString(), "getContent");
                push(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
