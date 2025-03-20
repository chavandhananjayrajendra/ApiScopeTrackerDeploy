package com.api.tracker.service;

import com.api.tracker.entity.ApiHistory;
import com.api.tracker.repository.ApiHistoryRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;

@Service
public class ExcelReaderService {

    @Autowired
    private ApiHistoryRepository apiHistoryRepository;

    public void importExcelData(String filePath) throws IOException {
        FileInputStream file = new FileInputStream(new File(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        // Check last row index to append new entries
        int lastRowNum = sheet.getLastRowNum();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            String microservice = row.getCell(0).getStringCellValue();
            String apiName = row.getCell(2).getStringCellValue();
            int iterationCount = (int) row.getCell(3).getNumericCellValue();

            Integer maxIteration = apiHistoryRepository.getMaxIterationCount(microservice, apiName);
            if (maxIteration == null || iterationCount > maxIteration) {
                // Insert a new row
                Row newRow = sheet.createRow(++lastRowNum);
                newRow.createCell(0).setCellValue(microservice);
                newRow.createCell(2).setCellValue(apiName);
                newRow.createCell(3).setCellValue(iterationCount);
                newRow.createCell(4).setCellValue(row.getCell(4).getStringCellValue());
                newRow.createCell(5).setCellValue(row.getCell(5).getLocalDateTimeCellValue().toString());
                newRow.createCell(6).setCellValue(row.getCell(6).getLocalDateTimeCellValue().toString());
                newRow.createCell(7).setCellValue(row.getCell(7).getStringCellValue());
                newRow.createCell(8).setCellValue(row.getCell(8).getStringCellValue());
                newRow.createCell(9).setCellValue(row.getCell(9).getLocalDateTimeCellValue().toString());

                // Save to repository
                ApiHistory entry = new ApiHistory();
                entry.setMicroserviceName(microservice);
                entry.setApiName(apiName);
                entry.setIterationCount(iterationCount);
                entry.setApiStatus(row.getCell(4).getStringCellValue());
                entry.setDeliveryDate(row.getCell(5).getLocalDateTimeCellValue().toLocalDate());
                entry.setPlannedEndDate(row.getCell(6).getLocalDateTimeCellValue().toLocalDate());
                entry.setStatus(row.getCell(7).getStringCellValue());
                entry.setTesterName(row.getCell(8).getStringCellValue());
                entry.setModifiedAt(row.getCell(9).getLocalDateTimeCellValue());

                apiHistoryRepository.save(entry);
            }
        }

        // Write back to file
        file.close();
        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
