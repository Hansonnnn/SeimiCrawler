package com.hanz.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import cn.wanghaomiao.xpath.model.JXDocument;
import com.hanz.Utils.FileUtils;
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

    //@Override
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
     *
     *
     * @param response
     */

    public void getContent(Response response) {
        JXDocument doc = response.document();

        try {
            logger.info("urls {}", doc.sel("//div[@class='title']/h2/text()"));
            String title = doc.sel("//div[@class='title']/h2/text()").toString();
            String Content = doc.sel("//div[@class='artDet']/p/text()").toString();
            FileUtils.writeToFile(title,FILE_PATH,Content);


        } catch (Exception e) {
            logger.error("error :{}", e.getMessage());
        }
    }

    /**
     *
     *
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
