package ezz.abdulaziz.project3_popularmovies_stage2.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by EZZ on 2018-04-03.
 */

public class DBContract {

    public static final String AUTHORITY = "ezz.abdulaziz.project3_popularmovies_stage2";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public DBContract() {
    }

    public static final class DBSchema implements BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(PATH_MOVIES)
                        .build();

        public static final String TBL_MOVIE = "movies";
        public static final String CLM_MOVIE_ID = "movie_id";
        public static final String CLM_VOTE_COUNT = "vote_count";
        public static final String CLM_VOTE_AVERAGE = "vote_average";
        public static final String CLM_POPULARITY = "popularity";
        public static final String CLM_MOVIE_TITLE = "movie_title";
        public static final String CLM_OVERVIEW = "overview";
        public static final String CLM_POSTER_PATH = "poster_path";
        public static final String CLM_BACKDROP_PATH = "backdrop_path";
        public static final String CLM_RELEASE_DATE = "release_date";
        public static final String CLM_TIMESTAMP = "time_stamp";

        public static final String TBL_REVIEW = "reviews";
        public static final String CLR_REVIEW_ID = "review_id";
        public static final String CLR_AUTHOR = "author";
        public static final String CLR_CONTENT = "content";

        public static final String TBL_TRAILER = "trailers";
        public static final String CLT_KEY = "key";
        public static final String CLT_NAME = "name";

    }

}
