package com.example.google_book_app.domain;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class Book {

    private String mId;
    private String mTitle;
    private String mSubtitle;
    private String[] mAuthors;
    private String mDescription;
    private String mBuyLink;
    private String mThumbnailURL;

    public Book(String mId, String mTitle, @Nullable String mSubtitle, String[] mAuthors, String mDescription,
                @Nullable String buyLink, @Nullable String mThumbnailURL) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mSubtitle = mSubtitle;
        this.mAuthors = mAuthors;
        this.mDescription = mDescription;
        this.mBuyLink = buyLink;
        this.mThumbnailURL = mThumbnailURL;
    }

    public String getId() {
        return mId;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBuyLink() {
        return mBuyLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return mId.equals(book.mId) &&
                mTitle.equals(book.mTitle) &&
                Objects.equals(mSubtitle, book.mSubtitle) &&
                Arrays.equals(mAuthors, book.mAuthors) &&
                mDescription.equals(book.mDescription) &&
                Objects.equals(mBuyLink, book.mBuyLink) &&
                Objects.equals(mThumbnailURL, book.mThumbnailURL);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mId, mTitle, mSubtitle, mDescription, mBuyLink, mThumbnailURL);
        result = 31 * result + Arrays.hashCode(mAuthors);
        return result;
    }
}
