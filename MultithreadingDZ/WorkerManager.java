package MultithreadingDZ;

import java.util.List;

class WorkerManager{
    private static List<Worker> workerList;

    public static List<Worker> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(List<Worker> workerList) {
        this.workerList = workerList;
    }
}