package com.example.google_book_app.utils;

import com.example.google_book_app.BuildConfig;

public final class Constants {

    /** Base URL from Google Book API */
    public static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/";

    /** Constants for network call */
    public static final String QUERY = "android";
    public static final String PROJECTION = "lite";
    public static final int PAGE = 0;
    public static final int MAX_RESULTS = 20;
    public static final String API_KEY = BuildConfig.API_KEY;

    /** Number of threads for the pool*/
    public static final int NUMBER_OF_THREADS_THREE = 3;

    /** API Status code for invalid API key or Authentication failed */
    public static final int RESPONSE_CODE_API_STATUS = 401;

    /** Room Database name **/
    public static final String DATABASE_NAME = "books";

    /** Data Source Page control **/
    public static final int PREVIOUS_PAGE_KEY_ONE = 1;
    public static final int NEXT_PAGE_KEY_TWO = 2;
    public static final int PAGE_ONE = 1;
}
