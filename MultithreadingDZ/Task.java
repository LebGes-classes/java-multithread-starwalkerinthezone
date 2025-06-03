package MultithreadingDZ;

class Task{
    private long id;
    private String meaning;
    private int neededHours;
    private int remainHours;
    private int worker_id;
    private String status;

    public Task(long id, String meaning, int neededHours, int remainHours, int worker_id, String status) {
        this.id = id;
        this.meaning = meaning;
        this.neededHours = neededHours;
        this.remainHours = remainHours;
        this.worker_id = worker_id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public int getNeededHours() {
        return neededHours;
    }

    public void setNeededHours(int neededHours) {
        this.neededHours = neededHours;
    }

    public int getRemainHours() {
        return remainHours;
    }

    public void setRemainHours(int remainHours) {
        this.remainHours = remainHours;
    }

    public int getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(int worker_id) {
        this.worker_id = worker_id;
    }

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}