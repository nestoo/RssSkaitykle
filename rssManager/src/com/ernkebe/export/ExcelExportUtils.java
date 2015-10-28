package com.ernkebe.export;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;

import com.ernkebe.entities.grouping.CustomArticle;
import com.ernkebe.entities.grouping.CustomGroupInfo;

public class ExcelExportUtils {
	
	
	public static InputStream createExcelFileCustomArticles(String title, List<CustomGroupInfo> groupArticles){
		//1. Create a new Workbook
        Workbook wb = new XSSFWorkbook();
        int rownumebr = 0;
        //2. Create a new sheet
        Sheet sheet = wb.createSheet("sheet 1");
 
        //3. Create a row
 
        //4. Create cells 
 
        //4.1 number cell
        Row rowTitles = sheet.createRow((short) rownumebr++);
        rowTitles.createCell(0).setCellValue("Grupės pavadinimas");
        rowTitles.createCell(1).setCellValue("Naujienų skaičius");
        rowTitles.createCell(2).setCellValue("Naujienos");
     
        for (CustomGroupInfo article : groupArticles) {
        	 Row r = sheet.createRow((short) rownumebr++);
        	 r.createCell(0).setCellValue(article.getTitle());
        	 r.createCell(1).setCellValue(article.getCount());

        	 r.createCell(2).setCellValue(Jsoup.parse(article.getNews()).text());
		}
        
        //5. create excel file
        FileOutputStream fileOut = null;
        try {
             
            fileOut = new FileOutputStream("workbook.xlsx");
            wb.write(fileOut);
            fileOut.close();
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        System.out.println( "File created!" );
        FileInputStream file;
		try {
			file = new FileInputStream("workbook.xlsx");
		} catch (FileNotFoundException e) {
			file = null;
			e.printStackTrace();
		}
		return  file;
 
	}
}
