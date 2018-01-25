package com.hanz.utils;

import com.hanz.model.NLM;

import java.io.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author hanzhao
 *         Created by hanzhao on 2017/6/13.
 */
public class FileUtils {

    /**
     * @param FileName
     * @param FilePath
     * @param content
     * @author hanzhao
     */
    public static void writeToFile(String FileName, String FilePath, String content) {

        try {
            File file = new File(FilePath + "article.json");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter out = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(out);
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            sb.append("" + "" + "" + "title" + "");
            sb.append("" + ":" + "");
            sb.append("" + FileName + "");
            sb.append("" + "," + "");
            sb.append("" + "content" + "");
            sb.append("" + ":" + "");
            sb.append("" + content + "");
            bw.write(sb.toString());
            sb.append("}");
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    /**
     * @param filePath
     * @return
     * @author hanzhao
     */
    public static List<String> readFileAsList(String filePath) {
        List<String> list = new ArrayList<String>();
        try {
            String encoding = "utf-8";
            File file = new File(filePath);
            if (file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    list.add(line);
                }
                bufferedReader.close();
                read.close();
            } else {
                System.out.println("can not find file! please try it" + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param filePath
     * @param fileName
     * @param fileType
     * @param drug
     * @throws IOException
     * @author hanzhao
     */
    public static void writeToExcel(String filePath, String fileName, String fileType, NLM drug) throws IOException {

        String[] titleRow = {"序号", "药品分类", "药品名称", "英文对照", "CAS", "Canonical SMILES", "Molecular Formula", "MeSH Entry Terms", "Depositor-Supplied Synonyms"};
        Workbook wb = null;
        String excelPath = filePath + File.separator + fileName + "." + fileType;
        File file = new File(excelPath);
        Sheet sheet = null;
        if (!file.exists()) {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();

            } else if (fileType.equals("xlsx")) {

                wb = new XSSFWorkbook();
            } else {
                return;
            }
            sheet = (Sheet) wb.createSheet("sheet1");
            OutputStream outputStream = new FileOutputStream(excelPath);
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();

        }
        if (sheet == null) {
            sheet = (Sheet) wb.createSheet("sheet1");
        }
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        row.setHeight((short) 540);
        cell.setCellValue("drug list");

        CellStyle style = wb.createCellStyle();

        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);

        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        sheet.autoSizeColumn(5200);

        row = sheet.createRow(1);
        for (int i = 0; i < titleRow.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(titleRow[i]);
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, 20 * 256);
        }
        row.setHeight((short) 540);
        row = (Row) sheet.createRow(2);
        row.setHeight((short) 500);
        row.createCell(0).setCellValue(drug.getId());
        row.createCell(1).setCellValue(drug.getCategary());
        row.createCell(2).setCellValue(drug.getZhName());
        row.createCell(3).setCellValue(drug.getEnName());
        row.createCell(4).setCellValue(drug.getCAS());
        row.createCell(5).setCellValue(drug.getCanonical());
        row.createCell(6).setCellValue(drug.getDepositor());
        row.createCell(7).setCellValue(drug.getMeSH());
        row.createCell(7).setCellValue(drug.getMolecula());
        OutputStream stream = new FileOutputStream(excelPath);
        wb.write(stream);
        stream.close();
    }
}
