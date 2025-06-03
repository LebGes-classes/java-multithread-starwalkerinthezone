package MultithreadingDZ;

import java.time.DayOfWeek;
import java.util.List;

public class Day implements Runnable {
    Object flag;
    public Day(Object flag){
        this.flag = flag;
    }
        private void startingDay(){
        ExcelParser excelParser = new ExcelParser();
        TaskManager taskManager = new TaskManager();
        WorkerManager workerManager = new WorkerManager();
        excelParser.reading(FilePaths.JOBS.getPath());
        taskManager.setTaskList(excelParser.getTaskList());
        workerManager.setWorkerList(excelParser.getWorkerList());
        List<Worker> workerList = workerManager.getWorkerList();
        List<Task> taskList = taskManager.getTaskList();
        for (Worker worker : workerList){
            worker.setTaskList(taskList);
        }
        for (Worker worker : workerList) {
            Thread thread = new Thread(worker);
            thread.start();
            }

    }

    @Override
    public void run() {
            synchronized (flag) {
                startingDay();
                flag.notify();
            }
    }
}
