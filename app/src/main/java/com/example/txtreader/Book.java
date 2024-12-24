package com.example.txtreader;


import com.google.gson.annotations.SerializedName;

public class Book {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("author")
    private String author;

    @SerializedName("coverUrl")
    private String coverUrl="";

    @SerializedName("filePath")
    private String filePath;
    public Book(int id, String title, String author, String coverUrl,String filePath) {
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.id=id;
        this.filePath=filePath;
    }
    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getFilePath() {
        return filePath;
    }
}

// public class Book {
//    private String title;
//    private String author;
//    private String coverUrl;
//
//    public Book(String title, String author, String coverUrl) {
//        this.title = title;
//        this.author = author;
//        this.coverUrl = coverUrl;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public String getCoverUrl() {
//        return coverUrl;
//    }
//}