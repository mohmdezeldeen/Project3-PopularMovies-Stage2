package ezz.abdulaziz.project3_popularmovies_stage2.rest;

import ezz.abdulaziz.project3_popularmovies_stage2.model.MoviesResponse;
import ezz.abdulaziz.project3_popularmovies_stage2.model.ReviewsResponse;
import ezz.abdulaziz.project3_popularmovies_stage2.model.TrailersResponse;
import ezz.abdulaziz.project3_popularmovies_stage2.utils.NetworkUtils;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET(NetworkUtils.POPULAR_PATH)
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET(NetworkUtils.TOP_RATED_PATH)
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET(NetworkUtils.TRAILERS)
    Call<TrailersResponse> getListOfTrailers(@Path("movie_id") int id,
                                             @Query("api_key") String apiKey);

    @GET(NetworkUtils.REVIEWS)
    Call<ReviewsResponse> getListOfReviews(@Path("movie_id") int id,
                                           @Query("api_key") String apiKey);
}
