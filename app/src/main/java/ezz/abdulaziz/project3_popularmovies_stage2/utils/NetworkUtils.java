package ezz.abdulaziz.project3_popularmovies_stage2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import ezz.abdulaziz.project3_popularmovies_stage2.BuildConfig;

/**
 * Created by EZZ on 2/27/2018.
 */

public class NetworkUtils {

    public static final String API_KEY = BuildConfig.API_KEY;

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String POPULAR_PATH = "movie/popular?api_key=";
    public static final String TOP_RATED_PATH = "movie/top_rated?api_key=";
    public static final String TRAILERS = "movie/{movie_id}/videos";
    public static final String REVIEWS = "movie/{movie_id}/reviews";
    public static final String POSTER_URL = "http://image.tmdb.org/t/p/";

    public static final String TRAILER_THUMBNAIL_URL = "http://img.youtube.com/vi/";
    public static final String TRAILER_THUMBNAIL_URL2 = "/mqdefault.jpg";

    public static final String YOUTUBE_WEB_URL = "https://www.youtube.com/watch?v=";
    public static final String YOUTUBE_APP_URL = "vnd.youtube:";


    public enum PosterSize {
        w92, w154, w185, w342, w500, w780, original
    }

    public enum ShowType {
        Popular, TopRated, Favorite
    }

    public static boolean isConnectionAvailable(@NonNull Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

}
