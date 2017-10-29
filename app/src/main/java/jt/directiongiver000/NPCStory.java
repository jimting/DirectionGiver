package jt.directiongiver000;

/**
 * Created by lp123 on 2017/10/26.
 */

public class NPCStory
{
    private String id;
    private String mp3URL;
    private String content;
    private String status;

    NPCStory(String id,String mp3URL,String content,String status)
    {
        this.id = id;
        this.mp3URL = mp3URL;
        this.content = content;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMp3URL() {
        return mp3URL;
    }

    public void setMp3URL(String mp3URL) {
        this.mp3URL = mp3URL;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
