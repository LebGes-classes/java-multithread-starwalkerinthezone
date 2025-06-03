package MultithreadingDZ;

import java.util.List;

public class TaskManager{
    private static List<Task> taskList;

    public static List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

}