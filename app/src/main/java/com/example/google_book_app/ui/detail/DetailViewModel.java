package com.example.google_book_app.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.google_book_app.database.BookEntry;
import com.example.google_book_app.repository.BookRepository;

public class DetailViewModel extends ViewModel {
    private final BookRepository mRepository;
    private LiveData<BookEntry> mBookEntry;

    public DetailViewModel(BookRepository repository, String bookId) {
        mRepository = repository;
        mBookEntry = mRepository.getFavoriteBookById(bookId);
    }

    public LiveData<BookEntry> getBookEntry() {
        return mBookEntry;
    }


}
