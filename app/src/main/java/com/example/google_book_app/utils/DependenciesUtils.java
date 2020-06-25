package com.example.google_book_app.utils;

import android.content.Context;

import com.example.google_book_app.database.BookDatabase;
import com.example.google_book_app.network.GoogleBookAPI;
import com.example.google_book_app.network.RetrofitServiceController;
import com.example.google_book_app.repository.BookRepository;
import com.example.google_book_app.ui.detail.DetailViewModelFactory;
import com.example.google_book_app.ui.main.MainViewModelFactory;

public class DependenciesUtils {

    public static MainViewModelFactory provideMainViewModelFactory(Context context, String filterBy) {
        BookRepository repository = getRepository(context.getApplicationContext());
        return new MainViewModelFactory(filterBy, repository);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, String bookId) {
        BookRepository repository = getRepository(context.getApplicationContext());
        return new DetailViewModelFactory(repository, bookId);
    }

    public static BookRepository getRepository(Context context) {
        BookDatabase database = BookDatabase.getInstance(context.getApplicationContext());
        AppThreadExecutors executors = AppThreadExecutors.getInstance();
        GoogleBookAPI googleBookAPI = RetrofitServiceController.getClient().create(GoogleBookAPI.class);
        return BookRepository.getInstance(database.bookDao(), googleBookAPI, executors);
    }
}
