package com.example.google_book_app.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.google_book_app.R;
import com.example.google_book_app.databinding.BookListItemBinding;
import com.example.google_book_app.domain.Book;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class BookPagedListAdapter extends PagedListAdapter<Book, BookPagedListAdapter.BookPagedViewHolder> {

    private final BookPagedListAdapter.BookPagedListAdapterOnClickHandler mOnClickHandler;

    public interface BookPagedListAdapterOnClickHandler {
        void onItemClick(Book book);
    }

    private static DiffUtil.ItemCallback<Book> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Book>() {
                @Override
                public boolean areItemsTheSame(Book oldItem, Book newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(Book oldItem, Book newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public BookPagedListAdapter(BookPagedListAdapterOnClickHandler onClickHandler) {
        super(BookPagedListAdapter.DIFF_CALLBACK);
        mOnClickHandler = onClickHandler;
    }

    @NonNull
    @Override
    public BookPagedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        BookListItemBinding mBookItemBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.book_list_item, parent, false);

        return new BookPagedViewHolder(mBookItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookPagedViewHolder holder, int position) {
        holder.bind(Objects.requireNonNull(getItem(position)));
    }

    public class BookPagedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private BookListItemBinding mBookItemBinding;

        public BookPagedViewHolder(BookListItemBinding bookItemBinding) {
            super(bookItemBinding.getRoot());
            mBookItemBinding = bookItemBinding;

            itemView.setOnClickListener(this);
        }

        void bind(Book book) {
            String thumbnail = book.getThumbnailURL();
            thumbnail = thumbnail.replaceFirst("^(http://)?(www\\.)?", "https://");
            Picasso.with(itemView.getContext())
                    .load(thumbnail)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(mBookItemBinding.ivThumbnail);

            mBookItemBinding.tvTitle.setText(book.getTitle());
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Book book = getItem(adapterPosition);
            mOnClickHandler.onItemClick(book);
        }
    }
}