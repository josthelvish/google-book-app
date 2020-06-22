package com.example.google_book_app.repository;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.example.google_book_app.domain.Book;

public class BookDataSourceFactory extends DataSource.Factory<Integer, Book> {
    @NonNull
    @Override
    public DataSource<Integer, Book> create() {
        return null;
    }
}
