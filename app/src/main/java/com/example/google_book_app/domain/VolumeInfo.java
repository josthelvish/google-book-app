package com.example.google_book_app.domain;

import com.google.gson.annotations.SerializedName;

public class VolumeInfo {
    @SerializedName("title")
    private String mTitle;

    @SerializedName("subtitle")
    private String mSubtitle;

    @SerializedName("authors")
    private String[] mAuthors;

    @SerializedName("description")
    private String description;
}
