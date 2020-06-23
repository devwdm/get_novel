package entity;//package gd.hwl.entity;

import java.util.Date;

public class Fiction {
    private int id;
    private String name;
    private String author;
    private String type;
    private String state;
    private String wordNumber;
    private Date lastestupdate;
    private String lastestChapter;
    private String intro;
    private String url;
    private Date crawLastTime;


    @Override
    public String toString() {
        return "Fiction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", type='" + type + '\'' +
                ", state='" + state + '\'' +
                ", wordNumber='" + wordNumber + '\'' +
                ", lastestupdate=" + lastestupdate +
                ", lastestChapter='" + lastestChapter + '\'' +
                ", intro='" + intro + '\'' +
                ", url='" + url + '\'' +
                ", crawLastTime=" + crawLastTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWordNumber() {
        return wordNumber;
    }

    public void setWordNumber(String wordNumber) {
        this.wordNumber = wordNumber;
    }

    public Date getLastestupdate() {
        return lastestupdate;
    }

    public void setLastestupdate(Date lastestupdate) {
        this.lastestupdate = lastestupdate;
    }

    public String getLastestChapter() {
        return lastestChapter;
    }

    public void setLastestChapter(String lastestChapter) {
        this.lastestChapter = lastestChapter;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCrawLastTime() {
        return crawLastTime;
    }

    public void setCrawLastTime(Date crawLastTime) {
        this.crawLastTime = crawLastTime;
    }
}
