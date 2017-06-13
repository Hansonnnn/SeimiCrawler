package com.hanz.Utils;

import java.io.*;

/**
 * Created by hanzhao on 2017/6/13.
 */
public class FileUtils {

    public static void writeToFile(String FileName,String FilePath,String content){

        try
        {
            File file=new File(FilePath+"article.json");
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter  out=new FileWriter(file,true);
            BufferedWriter bw= new BufferedWriter(out);
            bw.write("{title:"+FileName+",content:"+content+"}");
            bw.newLine();
            bw.flush();
            bw.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
    }
}
