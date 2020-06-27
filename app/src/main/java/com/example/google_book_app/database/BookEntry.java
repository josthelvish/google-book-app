package com.example.google_book_app.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "book")
public class BookEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "book_id")
    private String bookId;

    @ColumnInfo(name = "book_title")
    private String mTitle;

    @ColumnInfo(name = "book_subtitle")
    private String mSubtitle;

    @ColumnInfo(name = "book_authors")
    private String mAuthors;

    @ColumnInfo(name = "book_description")
    private String mDescription;

    @ColumnInfo(name = "book_buy_link")
    private String mBuyLink;

    @ColumnInfo(name = "book_thumbnail_url")
    private String mThumbnailURL;

    public BookEntry(String bookId, String mTitle, String mSubtitle, String mAuthors,
                     String mDescription, String mBuyLink, String mThumbnailURL) {
        this.bookId = bookId;
        this.mTitle = mTitle;
        this.mSubtitle = mSubtitle;
        this.mAuthors = mAuthors;
        this.mDescription = mDescription;
        this.mBuyLink = mBuyLink;
        this.mThumbnailURL = mThumbnailURL;
    }

    public int getId() {
        return id;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getBuyLink() {
        return mBuyLink;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public void setAuthors(String mAuthors) {
        this.mAuthors = mAuthors;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setBuyLink(String mBuyLink) {
        this.mBuyLink = mBuyLink;
    }

    public void setThumbnailURL(String mThumbnailURL) {
        this.mThumbnailURL = mThumbnailURL;
    }
}
