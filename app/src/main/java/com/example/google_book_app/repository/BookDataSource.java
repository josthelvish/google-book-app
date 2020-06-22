package com.example.google_book_app.repository;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.google_book_app.domain.Book;

public class BookDataSource extends PageKeyedDataSource<Integer, Book> {
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Book> callback) {

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Book> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Book> callback) {

    }
}
