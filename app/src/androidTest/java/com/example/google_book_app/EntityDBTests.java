package com.example.google_book_app;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.google_book_app.database.BookDao;
import com.example.google_book_app.database.BookDatabase;
import com.example.google_book_app.database.BookEntry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;


@RunWith(AndroidJUnit4.class)
public class EntityDBTests {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
    @Mock
    private Observer<BookEntry> observer;

    private BookDao bookDao;
    private BookDatabase db;

    @Before
    public void createDb() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, BookDatabase.class)
                .allowMainThreadQueries().build();
        bookDao = db.bookDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertingBook_BookIsDatabase() throws Exception {
        // arrange
        String bookId = "bookId";
        BookEntry bookEntry = new BookEntry(bookId, "mTitle", "mSubtitle",
                "mAuthors", "mDescription", "mBuyLink",
                "mThumbnailURL");
        // act
        bookDao.insertBook(bookEntry);
        bookDao.loadBookById(bookId).observeForever(observer);
        // assert
        /*ArgumentCaptor<BookEntry> captor = ArgumentCaptor.forClass(BookEntry.class);
        Mockito.verify(observer, times(1)).onChanged(captor.capture());
        BookEntry result = captor.getValue();
        assertEquals(bookEntry, result);*/
        Mockito.verify(observer).onChanged(bookEntry);
    }
}

