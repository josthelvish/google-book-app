package com.example.google_book_app.repository;

import com.example.google_book_app.database.BookDao;
import com.example.google_book_app.network.GoogleBookAPI;
import com.example.google_book_app.utils.AppThreadExecutors;

import timber.log.Timber;

public class BookRepository {

    private static final Object LOCK = new Object();
    private static BookRepository sInstance;
    private final BookDao mBookDao;
    private final GoogleBookAPI mGoogleBookApi;
    private final AppThreadExecutors mExecutors;

    private BookRepository(BookDao bookDao,
                           GoogleBookAPI googleBookApi,
                           AppThreadExecutors executors) {
        mBookDao = bookDao;
        mGoogleBookApi = googleBookApi;
        mExecutors = executors;
    }

    public synchronized static BookRepository getInstance(
            BookDao bookDao, GoogleBookAPI googleBookAPI, AppThreadExecutors executors) {
        Timber.d("Getting repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                Timber.d("Creating new repository");
                sInstance = new BookRepository(bookDao, googleBookAPI, executors);
            }
        }
        return sInstance;
    }
}
