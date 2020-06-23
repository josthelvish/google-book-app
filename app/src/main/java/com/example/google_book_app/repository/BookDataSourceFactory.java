package com.example.google_book_app.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.google_book_app.domain.Book;

public class BookDataSourceFactory extends DataSource.Factory<Integer, Book> {

    private MutableLiveData<BookDataSource> mPostLiveData;
    private BookDataSource mBookDataSource;

    public BookDataSourceFactory() {
        mPostLiveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource<Integer, Book> create() {
        mBookDataSource = new BookDataSource();

        mPostLiveData = new MutableLiveData<>();
        mPostLiveData.postValue(mBookDataSource);

        return mBookDataSource;
    }

    public MutableLiveData<BookDataSource> getPostLiveData() {
        return mPostLiveData;
    }
}
