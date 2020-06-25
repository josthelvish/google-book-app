package com.example.google_book_app.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class Book implements Parcelable {

    private String mId;
    private String mTitle;
    private String mSubtitle;
    private String[] mAuthors;
    private String mDescription;
    private String mBuyLink;
    private String mThumbnailURL;

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {

        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public Book(String mId, String mTitle, String mSubtitle, String[] mAuthors, String mDescription,
                String buyLink, String mThumbnailURL) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mSubtitle = mSubtitle;
        this.mAuthors = mAuthors;
        this.mDescription = mDescription;
        this.mBuyLink = buyLink;
        this.mThumbnailURL = mThumbnailURL;
    }

    private Book(Parcel in) {
        int authorArraySize = in.readInt();
        mId = in.readString();
        mTitle = in.readString();
        mSubtitle = in.readString();
        mAuthors = new String[authorArraySize];
        in.readStringArray(mAuthors);
        mDescription = in.readString();
        mBuyLink = in.readString();
        mThumbnailURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mAuthors.length);
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mSubtitle);
        dest.writeStringArray(mAuthors);
        dest.writeString(mDescription);
        dest.writeString(mBuyLink);
        dest.writeString(mThumbnailURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return mId;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getSubtitle() {
        return mSubtitle;
    }

    @Nullable
    public String getBuyLink() {
        return mBuyLink;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getAuthors() {
        return TextUtils.join("\n", mAuthors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return mId.equals(book.mId) &&
                mTitle.equals(book.mTitle) &&
                Objects.equals(mSubtitle, book.mSubtitle) &&
                Arrays.equals(mAuthors, book.mAuthors) &&
                mDescription.equals(book.mDescription) &&
                Objects.equals(mBuyLink, book.mBuyLink) &&
                Objects.equals(mThumbnailURL, book.mThumbnailURL);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mId, mTitle, mSubtitle, mDescription, mBuyLink, mThumbnailURL);
        result = 31 * result + Arrays.hashCode(mAuthors);
        return result;
    }
}
