package com.example.google_book_app.network;

import androidx.annotation.Nullable;

import com.example.google_book_app.network.BookImageLinksDTO;
import com.google.gson.annotations.SerializedName;

public class BookVolumeInfoDTO {
    @SerializedName("title")
    private String mTitle;

    @SerializedName("subtitle")
    private String mSubtitle;

    @SerializedName("authors")
    private String[] mAuthors;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("imageLinks")
    @Nullable
    private BookImageLinksDTO mImageLinks;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public String[] getAuthors() {
        return mAuthors;
    }

    public void setAuthors(String[] mAuthors) {
        this.mAuthors = mAuthors;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    @Nullable
    public BookImageLinksDTO getImageLinks() {
        return mImageLinks;
    }

    public void setImageLinks(BookImageLinksDTO imageLinks) {
        this.mImageLinks = imageLinks;
    }
}
