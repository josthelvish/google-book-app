package com.example.google_book_app.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.google_book_app.database.BookEntry;
import com.example.google_book_app.domain.Book;
import com.example.google_book_app.repository.BookDataSourceFactory;
import com.example.google_book_app.repository.BookRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.google_book_app.utils.Constants.INITIAL_LOAD_SIZE_HINT;
import static com.example.google_book_app.utils.Constants.NUMBER_OF_THREADS_THREE;
import static com.example.google_book_app.utils.Constants.PAGE_SIZE;
import static com.example.google_book_app.utils.Constants.PREFETCH_DISTANCE;

public class MainActivityViewModel extends ViewModel {

    private final BookRepository mRepository;

    private LiveData<PagedList<Book>> mBookPagedList;
    private LiveData<List<BookEntry>> mFavoriteBooks;

    private String mFilterListBy;

    public MainActivityViewModel(String filterListBy, BookRepository repository) {
        mFilterListBy = filterListBy;
        mRepository = repository;
        init(mFilterListBy);
    }

    private void init(String mFilterListBy) {
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS_THREE);

        BookDataSourceFactory bookDataFactory = new BookDataSourceFactory(mFilterListBy);

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build();

        mBookPagedList = new LivePagedListBuilder<>(bookDataFactory, config)
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<PagedList<Book>> getBookPagedList() {
        return mBookPagedList;
    }

    public void setBookPagedList(String filterBy) {
        init(mFilterListBy);
    }

    public LiveData<List<BookEntry>> getFavoriteBooks() {
        return mFavoriteBooks;
    }

    public void setFavoriteBooks() {
        mFavoriteBooks = mRepository.getFavoriteBooks();
    }
}
