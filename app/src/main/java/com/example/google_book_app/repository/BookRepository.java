package com.example.google_book_app.repository;

import androidx.lifecycle.LiveData;

import com.example.google_book_app.database.BookDao;
import com.example.google_book_app.database.BookEntry;

import java.util.List;

import timber.log.Timber;

public class BookRepository {

    private static final Object LOCK = new Object();
    private static BookRepository sInstance;
    private final BookDao mBookDao;

    private BookRepository(BookDao bookDao) {
        mBookDao = bookDao;
    }

    public synchronized static BookRepository getInstance(
            BookDao bookDao) {
        Timber.d("Getting repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                Timber.d("Creating new repository");
                sInstance = new BookRepository(bookDao);
            }
        }
        return sInstance;
    }

    public LiveData<List<BookEntry>> getFavoriteBooks() {
        return mBookDao.loadAllBooks();
    }

    public LiveData<BookEntry> getFavoriteBookById(String bookId) {
        return mBookDao.loadBookById(bookId);
    }

    public void addFavoriteBook(BookEntry book) {
        mBookDao.insertBook(book);
    }

    public void removeFavoriteBook(BookEntry book) {
        mBookDao.deleteBook(book);
    }
}
