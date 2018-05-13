package com.example.elena.onebooksapp;

/**
 * Created by Elena on 17/06/2017.
 */

public class Book {

    private String mTitle;
    private String mAuthor;

    public Book (String title, String author){
        mTitle = title;
        mAuthor = author;
    }

    public String getTitle(){return mTitle;}
    public String getAuthor(){return mAuthor;}

}
