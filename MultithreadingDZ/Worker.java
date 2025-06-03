package MultithreadingDZ;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class Worker implements Runnable {
    private final long id;
    private final String name;
    private String status;
    private List<Task> taskList = new ArrayList<>();
    private Consumer<String> messageSender;

    public Worker(long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public void setMessageSender(Consumer<String> messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void run() {
        int daytime = 9;
        int coffeeBreak = (int) (Math.random() * 4);

        try {
            while (daytime < 17 && !Thread.currentThread().isInterrupted()) {
                for (Task task : taskList) {
                    status = "активен";
                    task.setStatus("в процессе");

                    for (int i = task.getNeededHours() - 1; i >= 0; i--) {
                        if (daytime == 12) {
                            status = "неактивен (кофе-брейк)";
                            for (int j = 0; j < coffeeBreak; j++) {
                                sendStatus(daytime, task);
                                task.setRemainHours(task.getRemainHours() + 1);
                                daytime++;
                                sleep();
                            }
                            status = "активен";
                        }

                        sendStatus(daytime, task);
                        task.setNeededHours(i);
                        task.setRemainHours(task.getRemainHours() + 1);
                        daytime++;

                        if (daytime >= 17) return;
                        sleep();
                    }

                    task.setStatus("завершен");
                    status = "завершил задачу";
                    sendStatus(daytime, null);
                }

                while (daytime < 17) {
                    sendMessage(String.format("%s: %s, задача: нет, время: %d:00", name, status, daytime));
                    daytime++;
                    sleep();
                }
            }
        } finally {
            sendMessage(String.format("%s завершил работу в %d:00", name, daytime));
        }
    }

    private void sendStatus(int time, Task task) {
        String message = task == null ?
                String.format("%s: %s, задача: нет, время: %d:00", name, status, time) :
                String.format("%s: %s, задача: %s, время: %d:00", name, status, task.getMeaning(), time);

        sendMessage(message);
    }

    private void sendMessage(String text) {
        if (messageSender != null) {
            messageSender.accept(text);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public long getId() { return id; }
    public String getStatus() { return status; }
    public String getName() { return name; }
    public void setTaskList(List<Task> taskList) {
        for (Task task : taskList){
            if (task.getWorker_id() == id && task.getNeededHours()>0){
                this.taskList.add(task);
            }
        }
    }
}