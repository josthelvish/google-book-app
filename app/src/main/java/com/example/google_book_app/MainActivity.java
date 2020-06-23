package com.example.google_book_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PageKeyedDataSource;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;

import com.example.google_book_app.domain.Book;
import com.example.google_book_app.network.BookResponse;
import com.example.google_book_app.network.GoogleBookAPI;
import com.example.google_book_app.network.RetrofitServiceController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.google_book_app.utils.Constants.API_KEY;
import static com.example.google_book_app.utils.Constants.MAX_RESULTS;
import static com.example.google_book_app.utils.Constants.NEXT_PAGE_KEY_TWO;
import static com.example.google_book_app.utils.Constants.PAGE_ONE;
import static com.example.google_book_app.utils.Constants.PREVIOUS_PAGE_KEY_ONE;
import static com.example.google_book_app.utils.Constants.QUERY;
import static com.example.google_book_app.utils.Constants.REQUEST_CODE_DIALOG;
import static com.example.google_book_app.utils.Constants.RESPONSE_CODE_API_STATUS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            showNetworkDialog(isOnline());
        }

        /*GoogleBookAPI mGoogleBookApi = RetrofitServiceController.getClient().create(GoogleBookAPI.class);

        mGoogleBookApi.getBooks(API_KEY, QUERY, PAGE_ONE, MAX_RESULTS)
                .enqueue(new Callback<BookResponse>() {
                    @Override
                    public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                        if (response.isSuccessful()) {
                            Timber.d("Success response %s ", response.body().getBookResults().size());


                        } else if (response.code() == RESPONSE_CODE_API_STATUS) {
                            Timber.e("The Api key is invalid. Response code: %s", response.code());
                        } else {
                            Timber.e("Response Code: %s", response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<BookResponse> call, Throwable t) {
                        Timber.e("Error caused failure initializing a PageList: %s", t.getMessage());
                    }
                });*/
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void showNetworkDialog(final boolean isOnline) {
        if (!isOnline) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);

            builder.setTitle(getString(R.string.no_network_title));
            builder.setMessage(getString(R.string.no_network_message));
            builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(Settings.ACTION_SETTINGS), REQUEST_CODE_DIALOG);
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), null);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

}