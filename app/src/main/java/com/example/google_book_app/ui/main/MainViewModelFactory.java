package com.example.google_book_app.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.google_book_app.repository.BookRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final BookRepository mRepository;
    private final String mFilterBy;

    public MainViewModelFactory(String filterBy, BookRepository repository) {
        this.mFilterBy = filterBy;
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(mFilterBy, mRepository);
    }
}

