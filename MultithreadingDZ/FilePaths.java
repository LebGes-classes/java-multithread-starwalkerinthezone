package MultithreadingDZ;

public enum FilePaths {
    JOBS ("C:\\Users\\VladL\\IdeaProjects\\ClassRegister\\src\\MultithreadingDZ\\Job.xlsx");
    private final String path;

    FilePaths(String path){
        this.path = path;
    }

    public String getPath(){return path;}
}
