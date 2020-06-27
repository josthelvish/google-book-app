package com.example.google_book_app.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.google_book_app.R;
import com.example.google_book_app.database.BookEntry;
import com.example.google_book_app.databinding.BookListItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteBookAdapter extends RecyclerView.Adapter<FavoriteBookAdapter.FavoriteViewHolder> {

    private Context mContext;
    private final FavoriteOnClickHandler mOnClickHandler;
    private List<BookEntry> mFavoriteBooks;

    public interface FavoriteOnClickHandler {
        void onFavItemClick(BookEntry favoriteBook);
    }

    public FavoriteBookAdapter(Context context, FavoriteOnClickHandler onClickHandler) {
        mContext = context;
        mOnClickHandler = onClickHandler;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        BookListItemBinding favItemBinding = DataBindingUtil
                .inflate(layoutInflater, R.layout.book_list_item, parent, false);
        return new FavoriteViewHolder(favItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        BookEntry bookEntry = mFavoriteBooks.get(position);
        holder.bind(bookEntry);
    }

    @Override
    public int getItemCount() {
        if (mFavoriteBooks == null) return 0;
        return mFavoriteBooks.size();
    }

    public void setBooks(List<BookEntry> favoriteBooks) {
        mFavoriteBooks = favoriteBooks;
        notifyDataSetChanged();
    }

    public List<BookEntry> getFavoriteBooks() {
        return mFavoriteBooks;
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        BookListItemBinding mFavItemBinding;

        public FavoriteViewHolder(BookListItemBinding favItemBinding) {
            super(favItemBinding.getRoot());

            mFavItemBinding = favItemBinding;

            itemView.setOnClickListener(this);
        }

        void bind(BookEntry favoriteBook) {
            String thumbnail = favoriteBook.getThumbnailURL();
            thumbnail = thumbnail.replaceFirst("^(http://)?(www\\.)?", "https://");
            Picasso.with(itemView.getContext())
                    .load(thumbnail)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(mFavItemBinding.ivThumbnail);

            mFavItemBinding.tvTitle.setText(favoriteBook.getTitle());
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            BookEntry favoriteBook = mFavoriteBooks.get(adapterPosition);
            mOnClickHandler.onFavItemClick(favoriteBook);
        }
    }
}
