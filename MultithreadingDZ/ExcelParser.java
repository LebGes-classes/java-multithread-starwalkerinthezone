package MultithreadingDZ;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelParser{
    private List<Worker> workerList;
    private List<Task> taskList;


    public ExcelParser(){
        workerList = new ArrayList<>();
        taskList = new ArrayList<>();
    }
    public void reading(String path){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet){
            Cell cellid = row.getCell(0);
            Cell cellname = row.getCell(1);
            Cell cellstatus = row.getCell(2);
            Worker worker = new Worker((long) cellid.getNumericCellValue(),
                    cellname.getStringCellValue().trim(), cellstatus.getStringCellValue().trim());
            workerList.add(worker);
        }
        Sheet sheet2 = workbook.getSheetAt(1);
        for (Row row : sheet2){
            Cell cellid = row.getCell(0);
            Cell cellmeaning = row.getCell(1);
            Cell celltime = row.getCell(2);
            Cell cellremain = row.getCell(3);
            Cell cellworkerid = row.getCell(4);
            Cell cellstatus = row.getCell(5);
            Task task = new Task((long) cellid.getNumericCellValue(), cellmeaning.getStringCellValue().trim(),
                    (int) celltime.getNumericCellValue(), (int) cellremain.getNumericCellValue(),
                    (int) cellworkerid.getNumericCellValue(), cellstatus.getStringCellValue().trim());
            taskList.add(task);
        }
        try {
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Worker> getWorkerList() {
        return workerList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}