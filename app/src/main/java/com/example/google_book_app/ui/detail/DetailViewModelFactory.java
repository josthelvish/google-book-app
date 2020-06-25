package com.example.google_book_app.ui.detail;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.google_book_app.repository.BookRepository;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final BookRepository mRepository;
    private final String mBookId;

    public DetailViewModelFactory(BookRepository repository, String bookId) {
        mRepository = repository;
        mBookId = bookId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel(mRepository, mBookId);
    }
}