package com.hanz.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.http.HttpMethod;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import com.hanz.utils.FileUtils;
import com.hanz.dao.CrawlerDao;
import com.hanz.model.NLM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hanzhao
 *         Created by hanzhao on 2018/01/20
 */
@Crawler(name = "MyCrawler")
public class MyCrawler extends BaseSeimiCrawler {

    @Value("${seimiAgentHost}")
    private String seimiAgentHost;

    @Value("${seimiAgentPort}")
    private int seimiAgentPort;

    @Autowired
    private CrawlerDao crawlerDao;

    private List<Object> urls = new ArrayList<Object>();

    public static String FILE_PATH = "/Users/hanzhao/Downloads/crawl/";
    double count = 1;
    public static Map<Integer, String> keyName = new HashMap<Integer, String>();

    @Override
    public String[] startUrls() {
        String [] result =new String[]{"https://www.ncbi.nlm.nih.gov/pccompound/?term=Urokinase"};
//        List<String> startUrls = new ArrayList<String>();
//        List<String> drugs = FileUtils.readFileAsList("/Users/hanzhao/Downloads/drug.txt");
//        for (String drugName : drugs) {
//            String[] drug = drugName.split(",");
//            String suffix = drug[0];
//            startUrls.add("https://www.ncbi.nlm.nih.gov/pccompound/?term=" + suffix);
//        }
//        String[] array = new String[startUrls.size()];
//        String[] result = startUrls.toArray(array);

        return result;

    }

    @Override
    public String seimiAgentHost() {
        return this.seimiAgentHost;
    }

    @Override
    public int seimiAgentPort() {
        return this.seimiAgentPort;
    }

    /***
     * @description crawels works at here!
     * @param response
     */
    public void start(Response response) {
        JXDocument doc = response.document();
        String xpath = "//div[@id='maincontent']/div[@class='content']/div[5]/div[@class='rprt']/div[@class='rslt']/a/@href";
        try {
            String url = "";
            if (doc.sel(xpath).size() > 0) {
                url = doc.sel(xpath).get(0).toString();
            }

            Request seimiReq = Request.build(url, "getContent");
            push(seimiReq);
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }

    }

    /**
     * get all information from pages
     *
     * @param response
     */

    public void getContent(Response response) {
        JXDocument doc = response.document();
        try {

            String xpath_CAS = "//ol[@class='content-list level-2']/li[@id='CAS']/div[@class='section-content']/div[1]/text()";
            String xpath_Canonical = "//ol[@class='content-list level-2']/li[@id='Canonical-SMILES']/div[@class='section-content']/div[@class='section-content-item']/text()";
            String xpath_Molecula = "//ol[@class='content-list level-2']/li[@id='Molecular-Formula']/div[@class='section-content']/div[@class='section-content-item']/text()";
            String xpath_MeSH = "//ol[@class='content-list level-2']/li[@id='MeSH-Entry-Terms']/div[@class='section-content']/div[@class='section-content-item']/div//div/ol//li/text()";
            String xpath_Depositor = "//ol[@class='content-list level-2']/li[@id='Depositor-Supplied-Synonyms']/div[@class='section-content']/div[@class='section-content-item']/div//div/ol//li/a/text()";

            String cas = doc.sel(xpath_CAS).toString();
            String canonical = doc.sel(xpath_Canonical).toString();
            String molecula = doc.sel(xpath_Molecula).toString();
            String mesh = doc.sel(xpath_MeSH).toString();
            String deposutor = doc.sel(xpath_Depositor).toString();

            NLM nlm = new NLM();
//            nlm.setEnName(suffix);
//            nlm.setZhName(drugZh);
            nlm.setId(count);
            nlm.setCAS(cas);
            nlm.setCanonical(canonical);
            nlm.setMolecula(molecula);
            nlm.setMeSH(mesh);
            nlm.setDepositor(deposutor);

            FileUtils.writeToExcel(FILE_PATH, "drug_name", "xlsx", nlm);

        } catch (Exception e) {
            logger.error("error :{}", e.getMessage());
        }
    }


}

