package vn.com.ids.myachef.business.utils.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.dao.model.IngredientModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-export-import-excel-poi
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 30/03/20
 * Time: 08.15
 */
@Component
public class ExcelGenerator {

    /* export */
    public ByteArrayInputStream exportExcel(List<IngredientModel> ingredientModels) throws Exception {
        String[] columns = {"Id", "Name", "Quantity"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            CreationHelper creationHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Data Ingredients");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            //Row of Header
            Row headerRow = sheet.createRow(0);

            //Header
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }


            int rowIdx = 1;
            for (IngredientModel ingredient : ingredientModels) {
                Row row = sheet.createRow(rowIdx);

                row.createCell(0).setCellValue(ingredient.getId());
                row.createCell(1).setCellValue(ingredient.getName());
                row.createCell(2).setCellValue(0);
                
                rowIdx++;
            }

            workbook.write(out);
            workbook.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            System.out.print("Export file exception: " + e);
        }
        return null;
    }
    
    public List<IngredientModel> importExcel(MultipartFile file) throws Exception {

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        
        List<IngredientModel> ingredientModels = new ArrayList<>();

        for (int i = 0; i < (CountRowExcel(sheet.rowIterator())); i++) {
            if (i == 0) {
                continue;
            }

            Row row = sheet.getRow(i);

            double id = row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            double quantity = row.getCell(2).getNumericCellValue();

            IngredientModel ingredientModel = new IngredientModel();
            ingredientModel.setId(Long.valueOf((long)id));
            ingredientModel.setName(name);
            ingredientModel.setQuantity(Double.valueOf(quantity));
            
            ingredientModels.add(ingredientModel);
        }
        
        return ingredientModels;

    }

    /* Cout Row of Excel Table */
    public static int CountRowExcel(Iterator<Row> iterator) {
        int size = 0;
        while (iterator.hasNext()) {
            Row row = iterator.next();
            size++;
        }
        return size;
    }

}
