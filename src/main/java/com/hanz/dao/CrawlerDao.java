package com.hanz.dao;


import com.hanz.model.Article;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

/**
 * Created by smith on 2017/3/16.
 */


public interface CrawlerDao {
    @Insert("insert into nuinformation (TITLE,CONTENT,URL) values (#{article.title},#{article.Content},#{article.url})")
    @Options(useGeneratedKeys = true, keyProperty = "article.id")
    int save(@Param("article")Article article);
}