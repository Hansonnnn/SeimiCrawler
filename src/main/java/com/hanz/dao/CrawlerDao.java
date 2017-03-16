package com.hanz.dao;


import com.hanz.model.Article;
import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
/**
 * Created by smith on 2017/3/16.
 */

@DAO
public interface CrawlerDao {
    @ReturnGeneratedKeys
    @SQL("insert into seimi (N_ID,S_TITLE,S_CONTENT,S_URL) values (:1.N_ID:1.S_TITLE,:1.S_CONTENT,:1.S_URL)")
    public int save(Article aritcle);
}