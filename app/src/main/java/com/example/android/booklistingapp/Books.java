package com.example.android.booklistingapp;

/**
 * Created by sksho on 11-Apr-17.
 */

public class Books {

    //default Books Title holder
    private final String title;
    //default Books Authors Name holder
    private final String authors;
    //default published date of the books holder
    private final String publishedDate;
    //default Preview Link holder
    private final String previewLink;

    /**
     * Constructor
     *
     * @param title
     * @param publishedDate
     * @param authors
     * @param previewLink
     */
    public Books(String title, String publishedDate, String authors, String previewLink) {
        this.title = title;
        this.publishedDate = publishedDate;
        this.authors = authors;
        this.previewLink = previewLink;
    }

    /**
     * getter for the books title
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * getter for books Authors Name
     *
     * @return Authors name
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * getter for getting published date of the books
     *
     * @return Published Date
     */
    public String getPublishedDate() {
        return publishedDate;
    }

    /**
     * getter for getting preview link
     *
     * @return Preview URL Link
     */
    public String getPreviewLink() {
        return previewLink;
    }
}
