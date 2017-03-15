package com.hanz.boot;

import cn.wanghaomiao.seimi.core.Seimi;

/**
 * Created by smith on 2017/3/13.
 */
public class Boot {
    public static void main(String[] args){
        Seimi s = new Seimi();
        s.start("seimiagent");
    }
}
