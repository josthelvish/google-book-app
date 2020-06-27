package com.example.google_book_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.google_book_app.database.BookEntry;
import com.example.google_book_app.database.BookPreferences;
import com.example.google_book_app.databinding.ActivityMainBinding;
import com.example.google_book_app.domain.Book;
import com.example.google_book_app.ui.SpacingItemDecoration;
import com.example.google_book_app.ui.detail.DetailActivity;
import com.example.google_book_app.ui.settings.SettingsActivity;
import com.example.google_book_app.ui.main.BookPagedListAdapter;
import com.example.google_book_app.ui.main.FavoriteBookAdapter;
import com.example.google_book_app.ui.main.MainActivityViewModel;
import com.example.google_book_app.ui.main.MainViewModelFactory;
import com.example.google_book_app.utils.DependenciesUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.example.google_book_app.utils.Constants.EXTRA_BOOK;
import static com.example.google_book_app.utils.Constants.GRID_INCLUDE_EDGE;
import static com.example.google_book_app.utils.Constants.GRID_SPACING;
import static com.example.google_book_app.utils.Constants.GRID_SPAN_COUNT;
import static com.example.google_book_app.utils.Constants.LAYOUT_MANAGER_STATE;
import static com.example.google_book_app.utils.Constants.REQUEST_CODE_DIALOG;

public class MainActivity extends AppCompatActivity implements
        FavoriteBookAdapter.FavoriteOnClickHandler,
        BookPagedListAdapter.BookPagedListAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private MainActivityViewModel mMainViewModel;
    private String mFilteredBy;
    private ActivityMainBinding mMainBinding;
    private BookPagedListAdapter mBookPagedListAdapter;
    private FavoriteBookAdapter mFavoriteBookAdapter;
    private Parcelable mSavedLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (savedInstanceState == null) {
            showNetworkDialog(isOnline());
        }

        initAdapter();

        mFilteredBy = BookPreferences.getFilterPreference(this);
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        createViewModel(mFilteredBy);
        refreshUI();
        setSwipeRefreshListener();
        customizeGridColumns();

        if (savedInstanceState != null) {
            mSavedLayoutState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
            Objects.requireNonNull(mMainBinding.rvBook.getLayoutManager()).onRestoreInstanceState(mSavedLayoutState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_key_filter_by))) {
            mFilteredBy = sharedPreferences.getString(key, getString(R.string.pref_filter_by_default));
        }

        mMainViewModel.setBookPagedList(mFilteredBy);
        refreshUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAYOUT_MANAGER_STATE,
                mMainBinding.rvBook.getLayoutManager().onSaveInstanceState());
    }

    private void initAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        mMainBinding.rvBook.setLayoutManager(layoutManager);
        mMainBinding.rvBook.setHasFixedSize(true);

        mBookPagedListAdapter = new BookPagedListAdapter(this);
        mFavoriteBookAdapter = new FavoriteBookAdapter(this, this);
    }

    private void customizeGridColumns() {
        SpacingItemDecoration decoration = new SpacingItemDecoration(GRID_SPAN_COUNT, GRID_SPACING,
                GRID_INCLUDE_EDGE);
        mMainBinding.rvBook.addItemDecoration(decoration);
    }

    private void setSwipeRefreshListener() {
        mMainBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showDataView();
                refreshUI();
                mMainBinding.swipeRefresh.setRefreshing(false);
                showSnackbarRefresh(isOnline());
            }
        });
    }

    private void showSnackbarRefresh(boolean isOnline) {
        if (isOnline) {
            Snackbar.make(mMainBinding.rvBook, getString(R.string.snackbar_updated)
                    , Snackbar.LENGTH_SHORT).show();
        }
    }

    private void refreshUI() {
        mMainViewModel.setFavoriteBooks();

        if (mFilteredBy.equals(getString(R.string.pref_filter_by_favorites))) {
            mMainBinding.rvBook.setAdapter(mFavoriteBookAdapter);
            observeFavorites();
        } else {
            mMainBinding.rvBook.setAdapter(mBookPagedListAdapter);
            observePagedList();
        }
    }

    private void observePagedList() {
        mMainViewModel.getBookPagedList().observe(this, new Observer<PagedList<Book>>() {
            @Override
            public void onChanged(@Nullable PagedList<Book> pagedList) {
                showDataView();
                if (pagedList != null) {
                    mBookPagedListAdapter.submitList(pagedList);
                    Timber.e("List size: %d", pagedList.size());
                    Objects.requireNonNull(mMainBinding.rvBook.getLayoutManager()).onRestoreInstanceState(mSavedLayoutState);
                }

                if (!isOnline()) {
                    showDataView();
                    showOfflineMessage();
                }
            }
        });
    }

    private void showOfflineMessage() {
        Snackbar snackbar = Snackbar.make(
                mMainBinding.frameMain, R.string.snackbar_offline, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.WHITE);
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.BLACK);
        snackbar.show();
    }

    private void observeFavorites() {
        mMainViewModel.getFavoriteBooks().observe(this, new Observer<List<BookEntry>>() {
            @Override
            public void onChanged(@Nullable List<BookEntry> favoriteBooks) {

                mFavoriteBookAdapter.setBooks(favoriteBooks);

                mMainBinding.rvBook.getLayoutManager().onRestoreInstanceState(mSavedLayoutState);

                if (favoriteBooks == null || favoriteBooks.size() == 0) {
                    showEmptyView();
                } else if (!isOnline()) {
                    showDataView();
                }
            }
        });
    }

    private void showDataView() {
        mMainBinding.tvEmpty.setVisibility(View.INVISIBLE);
        mMainBinding.rvBook.setVisibility(View.VISIBLE);
    }

    private void showEmptyView() {
        mMainBinding.tvEmpty.setVisibility(View.VISIBLE);
        mMainBinding.tvEmpty.setText(getString(R.string.empty_favorites_message));
        mMainBinding.tvEmpty.setCompoundDrawablesWithIntrinsicBounds(0,
                R.drawable.ic_baseline_menu_book_24, 0, 0);
        mMainBinding.tvEmpty.setTextColor(Color.WHITE);
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

    private void createViewModel(String filterBy) {
        MainViewModelFactory factory = DependenciesUtils.provideMainViewModelFactory(
                MainActivity.this, filterBy);
        mMainViewModel = new ViewModelProvider(this, factory).get(MainActivityViewModel.class);
    }

    @Override
    public void onFavItemClick(BookEntry favEntry) {
        String id = favEntry.getBookId();
        String title = favEntry.getTitle();
        String subtitle = favEntry.getSubtitle();
        String authors = favEntry.getAuthors();
        String description = favEntry.getDescription();
        String buyLink = favEntry.getBuyLink();
        String thumbnailURL = favEntry.getThumbnailURL();

        Book book = new Book(id, title, subtitle, authors.split("\n"), description, buyLink,
                thumbnailURL);

        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_BOOK, book);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        intent.putExtra(EXTRA_BOOK, bundle);
        startActivity(intent);

    }

    @Override
    public void onItemClick(Book book) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_BOOK, book);

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(EXTRA_BOOK, bundle);
        startActivity(intent);
    }
}