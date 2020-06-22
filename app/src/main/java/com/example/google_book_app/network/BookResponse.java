package com.example.google_book_app.network;

import com.example.google_book_app.domain.Book;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookResponse {

    @SerializedName("totalItems")
    private int mTotalPages;

    @SerializedName("items")
    private List<Book> mBookResults = null;

    @SuppressWarnings({"unused", "used by Retrofit"})
    public BookResponse() {
    }

    public List<Book> getBookResults() {
        return mBookResults;
    }
}
