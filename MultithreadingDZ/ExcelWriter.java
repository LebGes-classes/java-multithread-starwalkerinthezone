package MultithreadingDZ;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {
    public static void write(List<Worker> workerList, List<Task> taskList) {
        FileInputStream fis;
        Workbook workbook;
        try {
            fis = new FileInputStream(FilePaths.JOBS.getPath());
            workbook = new XSSFWorkbook(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

        Sheet sheet1 = workbook.getSheetAt(0);
        for (int i = 0; i < workerList.size(); i++){
            Row row = sheet1.getRow(i);
            row.getCell(2).setCellValue(workerList.get(i).getStatus());
        }
        Sheet sheet2 = workbook.getSheetAt(1);
        for (int i = 0; i < taskList.size(); i++){
            Row row = sheet2.getRow(i);
            Task task = taskList.get(i);
            row.getCell(2).setCellValue(task.getNeededHours());
            row.getCell(3).setCellValue(task.getRemainHours());
            row.getCell(5).setCellValue(task.isStatus());
        }
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(FilePaths.JOBS.getPath());
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            System.out.println("успешно обновлено");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
