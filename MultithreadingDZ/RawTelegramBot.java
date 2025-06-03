package MultithreadingDZ;

import java.io.*;
import java.net.*;
import java.util.*;

public class RawTelegramBot {
    private static final String BOT_TOKEN = "7231155864:AAG7Qbh8mI60c_zL_UDDaLpmgRU2rGAPwyg";
    private static final String BOT_API_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/";
    private int lastUpdateId = 0;
    private long currentChatId = 0;

    public void start() {
        System.out.println("Bot started. Waiting for /start_day...");

        while (true) {
            try {
                String updates = getUpdates(lastUpdateId+1);
                System.out.println("Updates received: " + updates);

                if (!updates.isEmpty() && updates.contains("\"update_id\":")) {
                    processUpdates(updates);


                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getUpdates(int offset) throws Exception {
        String urlString = BOT_API_URL + "getUpdates?offset=" + offset + "&timeout=10";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    private void processUpdates(String updates) {
        try {
            List<Integer> updateIds = new ArrayList<>();
            int lastIndex = 0;
            while (true) {
                int start = updates.indexOf("\"update_id\":", lastIndex);
                if (start == -1) break;
                start += 12; // Длина "\"update_id\":"
                int end = updates.indexOf(",", start);
                if (end == -1) end = updates.indexOf("}", start);
                String valueStr = updates.substring(start, end).trim();
                updateIds.add(Integer.parseInt(valueStr));
                lastIndex = end;
            }
            if (!updateIds.isEmpty()) {
                lastUpdateId = Collections.max(updateIds);
            }

            if (updates.contains("\"text\":\"/start_day\"")) {
                int textIndex = updates.indexOf("\"text\":\"/start_day\"");
                int chatStart = updates.lastIndexOf("\"chat\":{\"id\":", textIndex);
                if (chatStart != -1) {
                    int start = chatStart + 13; // Длина "\"chat\":{\"id\":"
                    int end = updates.indexOf(",", start);
                    currentChatId = Long.parseLong(updates.substring(start, end));
                }
                System.out.println("Начат рабочий день для чата: " + currentChatId);
                startWorkDay();
                currentChatId = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String text) {
        if (currentChatId == 0) {
            System.err.println("Error: chat_id is 0!");
            return;
        }

        try {
            String urlString = BOT_API_URL + "sendMessage";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                    "{\"chat_id\": %d, \"text\": \"%s\"}",
                    currentChatId,
                    text.replace("\"", "\\\"")
            );

            System.out.println("Sending: " + jsonInputString);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("HTTP Response: " + code);

        } catch (Exception e) {
            System.err.println("Failed to send message:");
            e.printStackTrace();
        }
    }


    private void startWorkDay() throws Exception {
        sendMessage("Начинаем рабочий день...");

        ExcelParser excelParser = new ExcelParser();
        TaskManager taskManager = new TaskManager();
        WorkerManager workerManager = new WorkerManager();

        excelParser.reading(FilePaths.JOBS.getPath());
        taskManager.setTaskList(excelParser.getTaskList());
        workerManager.setWorkerList(excelParser.getWorkerList());

        List<Worker> workers = workerManager.getWorkerList();
        for (Worker worker : workers) {
            worker.setTaskList(taskManager.getTaskList());
            worker.setMessageSender(this::sendMessage);
        }


        List<Thread> threads = new ArrayList<>();
        for (Worker worker : workers) {
            Thread thread = new Thread(worker);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (Thread thread : threads){
            thread.interrupt();
        }

        ExcelWriter.write(workerManager.getWorkerList(), taskManager.getTaskList());
        sendMessage("Рабочий день завершён. Данные сохранены.\nОтправьте /start_day для нового дня.");

    }
}
