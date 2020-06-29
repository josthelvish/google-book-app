package com.example.google_book_app.repository;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.google_book_app.domain.Book;
import com.example.google_book_app.network.BookResponseDTO;
import com.example.google_book_app.network.GoogleBookAPI;
import com.example.google_book_app.network.RetrofitServiceController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.google_book_app.utils.Constants.API_KEY;
import static com.example.google_book_app.utils.Constants.MAX_RESULTS;
import static com.example.google_book_app.utils.Constants.NEXT_PAGE_KEY_TWO;
import static com.example.google_book_app.utils.Constants.PAGE;
import static com.example.google_book_app.utils.Constants.PREVIOUS_PAGE_KEY_ONE;
import static com.example.google_book_app.utils.Constants.PROJECTION;
import static com.example.google_book_app.utils.Constants.QUERY;
import static com.example.google_book_app.utils.Constants.RESPONSE_CODE_API_STATUS;

public class BookDataSource extends PageKeyedDataSource<Integer, Book> {

    private GoogleBookAPI mGoogleBookApi;

    public BookDataSource() {
        mGoogleBookApi = RetrofitServiceController.getClient().create(GoogleBookAPI.class);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Book> callback) {
        mGoogleBookApi.getBooks(API_KEY, QUERY, PAGE, MAX_RESULTS, PROJECTION)
                .enqueue(new Callback<BookResponseDTO>() {
                    @Override
                    public void onResponse(Call<BookResponseDTO> call, Response<BookResponseDTO> response) {
                        if (response.isSuccessful()) {
                            callback.onResult(response.body().getBookResults(),
                                    PREVIOUS_PAGE_KEY_ONE, NEXT_PAGE_KEY_TWO);

                        } else if (response.code() == RESPONSE_CODE_API_STATUS) {
                            Timber.e("The Api key is invalid. Response code: %s",
                                    response.code());
                        } else {
                            Timber.e("Response Code: %s", response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<BookResponseDTO> call, Throwable t) {
                        Timber.e("Error caused failure initializing a PageList: %s",
                                t.getMessage());
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, Book> callback) {
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull final LoadCallback<Integer, Book> callback) {

        final int currentPage = params.key;
        mGoogleBookApi.getBooks(API_KEY, QUERY, currentPage, MAX_RESULTS, PROJECTION)
                .enqueue(new Callback<BookResponseDTO>() {
                    @Override
                    public void onResponse(Call<BookResponseDTO> call, Response<BookResponseDTO>
                            response) {
                        if (response.isSuccessful()) {
                            int nextKey = currentPage + NEXT_PAGE_KEY_TWO;
                            if (nextKey < response.body().getTotalItems()) {
                                callback.onResult(response.body().getBookResults(), nextKey);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BookResponseDTO> call, Throwable t) {
                        Timber.e("Failure appending the page: %s", t.getMessage());
                    }
                });
    }
}
