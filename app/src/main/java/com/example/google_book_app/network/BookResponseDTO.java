package com.example.google_book_app.network;

import com.example.google_book_app.domain.Book;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BookResponseDTO {

    @SerializedName("totalItems")
    private int mTotalItems;

    @SerializedName("items")
    private List<BookDTO> mBookResults = null;

    public BookResponseDTO(int totalItems) {
        mTotalItems = totalItems;
    }

    public int getTotalItems() {
        return mTotalItems;
    }

    public List<Book> getBookResults() {
        return convertBookDTO();
    }

    private List<Book> convertBookDTO() {
        List<Book> books = new ArrayList<>();

        for (BookDTO dto : mBookResults) {
            BookVolumeInfoDTO volumeInfo = dto.getVolumeInfo();
            BookSaleInfoDTO saleInfo = dto.getSaleInfo();
            String thumbnail = "";
            if (volumeInfo.getImageLinks() != null) {
                thumbnail = volumeInfo.getImageLinks().getThumbnail();
            }
            Book book = new Book(dto.getId(), volumeInfo.getTitle(), volumeInfo.getSubtitle(),
                    volumeInfo.getAuthors(), volumeInfo.getDescription(), saleInfo.getBuyLink(),
                    thumbnail);
            books.add(book);
        }
        return books;
    }
}
