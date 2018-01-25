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
 * Created by hanzhao on 2017/6/13.
 */
@Crawler(name="SeimiCrawler")
public class SeimiCrawler extends BaseSeimiCrawler {

    @Value("${seimiAgentHost}")
    private String seimiAgentHost;

    @Value("${seimiAgentPort}")
    private int seimiAgentPort;

    @Autowired
    private CrawlerDao crawlerDao;

    private List<Object> urls = new ArrayList<Object>();

    public static String FILE_PATH="/Users/hanzhao/Downloads/crawl/";

    @Override
    public String[] startUrls() {
        return new String[]{"http://health.people.com.cn/GB/373271/404183/"};
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
        String xPath = "//div[@id='main']/ul[@id='tiles']/li/h2/b/a/@href";
        try {
            urls.addAll(doc.sel(xPath));
            for (int i=2;i<6;i++) {
                Request seimiReq = Request.build("http://health.people.com.cn/GB/373271/404183/index"+i+".html", "getFirstPage");
                push(seimiReq);
            }

        } catch (Exception e) {
            logger.error("error{}", e.getMessage());
        }

    }

    /**
     *获取文章的标题及文本
     *
     * @param response
     */

    public void getContent(Response response) {
        JXDocument doc = response.document();
        String title = "";
        String content = "";
        try {
            logger.info("urls {}", doc.sel("//div[@class='title']/h2/text()"));

            /** 定义多种不同的xpath解析方式应对不同的页面结构*/

            String xpath1_title = "//div[@class='title']/h2/text()";
            String xpath2_title = "//div[@class='clearfix w1000_320 text_title']/h1/text()";
            String xpath2_content = "//div[@class='box_con']/p/text()";
            String xpath1_content = "//div[@class='artDet']/p/text()";

            if(doc.sel(xpath1_title)==null||doc.sel(xpath1_title).size()==0){
                title = doc.sel(xpath2_title).toString();
                content = doc.sel(xpath2_content).toString();
            }else{
                title = doc.sel(xpath1_title).toString();
                content = doc.sel(xpath1_content).toString();
            }
            Article article = new Article();
            article.setContent(content);
            article.setTitle(title);
            article.setUrl(response.getUrl());

            crawlerDao.save(article);

//            FileUtils.writeToFile(title,FILE_PATH,content);

        } catch (Exception e) {
            logger.error("error :{}", e.getMessage());
        }
    }

    /**
     *
     *获取第一层页面中所有的文章链接
     * @param response
     */

    public void getFirstPage(Response response) {
        JXDocument doc = response.document();
        String xPath = "//div[@id='main']/ul[@id='tiles']/li/h2/b/a/@href";
        try {
            urls.addAll(doc.sel(xPath));
            logger.info("urls {}", urls.size());

            for (Object s : urls) {
                Request req = Request.build("http://health.people.com.cn"+s.toString(), "getContent");
                push(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
