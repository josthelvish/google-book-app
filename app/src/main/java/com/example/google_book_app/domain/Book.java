package com.example.google_book_app.domain;

import com.google.gson.annotations.SerializedName;

public class Book {

    @SerializedName("id")
    private String mId;

    @SerializedName("volumeInfo")
    private VolumeInfo mVolumeInfo;

    @SerializedName("saleInfo")
    private SaleInfo mSaleInfo;

    public Book(String bookId, VolumeInfo volumeInfo, SaleInfo saleInfo) {
        mId = bookId;
        mVolumeInfo = volumeInfo;
        mSaleInfo = saleInfo;
    }
}
