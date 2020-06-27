package com.example.google_book_app.ui.detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.google_book_app.R;
import com.example.google_book_app.database.BookDatabase;
import com.example.google_book_app.database.BookEntry;
import com.example.google_book_app.databinding.ActivityDetailBinding;
import com.example.google_book_app.domain.Book;
import com.example.google_book_app.utils.AppThreadExecutors;
import com.example.google_book_app.utils.DependenciesUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import static com.example.google_book_app.utils.Constants.EXTRA_BOOK;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding mDetailBinding;
    private Book mBook;
    private boolean mIsFavorite;
    private DetailViewModel mDetailViewModel;
    private BookDatabase mBookDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mBookDB = BookDatabase.getInstance(this);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_BOOK)) {
                Bundle b = intent.getBundleExtra(EXTRA_BOOK);
                mBook = b.getParcelable(EXTRA_BOOK);
            }
        }

        createViewModel();
        observeBookEntry();
        refreshUI();

    }

    private void createViewModel() {
        DetailViewModelFactory factory = DependenciesUtils.provideDetailViewModelFactory(
                DetailActivity.this, mBook.getId());
        mDetailViewModel = new ViewModelProvider(this, factory).get(DetailViewModel.class);
    }

    private void observeBookEntry() {
        mDetailViewModel.getBookEntry().observe(this, new Observer<BookEntry>() {
            @Override
            public void onChanged(@Nullable BookEntry bookEntry) {
                if (mDetailViewModel.getBookEntry().getValue() == null) {
                    mDetailBinding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    mIsFavorite = false;
                } else {
                    mDetailBinding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                    mIsFavorite = true;
                }
            }
        });
    }

    public void onFavoriteClick(View view) {

        final BookEntry bookEntry = covertToBookEntry();

        if (!mIsFavorite) {
            AppThreadExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    // todo change to repository
                    mBookDB.bookDao().insertBook(bookEntry);
                }
            });

            showSnackbarMessage(R.string.snackbar_added);
        } else {
            final BookEntry bookEntry2 = mDetailViewModel.getBookEntry().getValue();
            AppThreadExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {

                    mBookDB.bookDao().deleteBook(bookEntry2);
                }
            });

            showSnackbarMessage(R.string.snackbar_removed);
        }
    }

    private BookEntry covertToBookEntry() {
        return new BookEntry(mBook.getId(), mBook.getTitle(), mBook.getSubtitle(), mBook.getAuthors(),
                mBook.getDescription(), mBook.getBuyLink(), mBook.getThumbnailURL());
    }

    private void refreshUI() {
        showUpButton();
        setCollapsingToolbarTitle();
        loadBookImage();
        setTextLabels();
    }

    private void showUpButton() {
        setSupportActionBar(mDetailBinding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void setCollapsingToolbarTitle() {
        mDetailBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mDetailBinding.collapsingToolbarLayout.setTitle(mBook.getTitle());
                    isShow = true;
                } else if (isShow) {
                    mDetailBinding.collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void loadBookImage() {
        String thumbnail = mBook.getThumbnailURL();
        thumbnail = thumbnail.replaceFirst("^(http://)?(www\\.)?", "https://");

        Picasso.with(this)
                .load(thumbnail)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(mDetailBinding.ivThumbnail);
    }

    private void setTextLabels() {

        setTitleLabels();
        setSubtitleLabels();
        setDescription();
        setAuthorLabels();
    }

    private void setAuthorLabels() {
        String author = mBook.getAuthors();
        mDetailBinding.tvAuthorText.setText(author);
    }

    private void setDescription() {
        String description = mBook.getDescription();
        mDetailBinding.tvDescriptionText.setText(description);
    }

    private void setTitleLabels() {
        String title = mBook.getTitle();
        mDetailBinding.tvDetailTitle.setText(title);
    }

    private void setSubtitleLabels() {
        String subtitle = mBook.getSubtitle();
        if (subtitle != null) {
            mDetailBinding.tvDetailSubtitle.setText(subtitle);
        }
    }

    private void showSnackbarMessage(int resourceMessageID) {
        Snackbar snackbar = Snackbar.make(
                mDetailBinding.coordinator, resourceMessageID, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.DKGRAY);
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

}
