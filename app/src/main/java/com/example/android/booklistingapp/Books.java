package com.example.android.booklistingapp;

/**
 * Created by sksho on 11-Apr-17.
 */

public class Books {

    private String mTitle;
    private String mAuthors;
    private String mPublishedDate;
    private String mPreviewLink;

    public Books(String title, String publishedDate, String authors, String previewLink) {
        this.mTitle = title;
        this.mPublishedDate = publishedDate;
        this.mAuthors = authors;
        this.mPreviewLink = previewLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getPreviewLink() {
        return mPreviewLink;
    }
}
