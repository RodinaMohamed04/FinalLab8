package Backend;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private String lessonId;
    private String title;
    private String content;
    private ArrayList<String> resources = new ArrayList<>();

    public Lesson(String lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
    }

    public String getLessonId() { return lessonId; }
    public String getTitle() { return title; }
    public String getContent() {
        return content; }
    public void setLessonId(String lessonId) { this.lessonId = lessonId; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }

    public ArrayList<String> getResources() {
        return resources; }
     public void setResources(ArrayList<String> resources) {
        this.resources = resources;
    }

    public void addResource(String resource) {
        resources.add(resource);
    }

    //public Object getResource() {
        // new UnsupportedOperationException("Not supported yet."); // Generated from 
    //}


}

