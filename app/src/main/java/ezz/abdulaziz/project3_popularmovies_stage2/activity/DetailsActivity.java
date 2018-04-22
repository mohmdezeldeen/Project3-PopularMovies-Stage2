package ezz.abdulaziz.project3_popularmovies_stage2.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ezz.abdulaziz.project3_popularmovies_stage2.R;
import ezz.abdulaziz.project3_popularmovies_stage2.adapter.ReviewAdapter;
import ezz.abdulaziz.project3_popularmovies_stage2.adapter.TrailerAdapter;
import ezz.abdulaziz.project3_popularmovies_stage2.model.Movie;
import ezz.abdulaziz.project3_popularmovies_stage2.model.Review;
import ezz.abdulaziz.project3_popularmovies_stage2.model.ReviewsResponse;
import ezz.abdulaziz.project3_popularmovies_stage2.model.Trailer;
import ezz.abdulaziz.project3_popularmovies_stage2.model.TrailersResponse;
import ezz.abdulaziz.project3_popularmovies_stage2.rest.ApiClient;
import ezz.abdulaziz.project3_popularmovies_stage2.rest.ApiInterface;
import ezz.abdulaziz.project3_popularmovies_stage2.utils.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ezz.abdulaziz.project3_popularmovies_stage2.database.DBContract.DBSchema.CLM_BACKDROP_PATH;
import static ezz.abdulaziz.project3_popularmovies_stage2.database.DBContract.DBSchema.CLM_MOVIE_ID;
import static ezz.abdulaziz.project3_popularmovies_stage2.database.DBContract.DBSchema.CLM_MOVIE_TITLE;
import static ezz.abdulaziz.project3_popularmovies_stage2.database.DBContract.DBSchema.CLM_OVERVIEW;
import static ezz.abdulaziz.project3_popularmovies_stage2.database.DBContract.DBSchema.CLM_POPULARITY;
import static ezz.abdulaziz.project3_popularmovies_stage2.database.DBContract.DBSchema.CLM_POSTER_PATH;
import static ezz.abdulaziz.project3_popularmovies_stage2.database.DBContract.DBSchema.CLM_RELEASE_DATE;
import static ezz.abdulaziz.project3_popularmovies_stage2.database.DBContract.DBSchema.CLM_VOTE_AVERAGE;
import static ezz.abdulaziz.project3_popularmovies_stage2.database.DBContract.DBSchema.CLM_VOTE_COUNT;
import static ezz.abdulaziz.project3_popularmovies_stage2.database.DBContract.DBSchema.CONTENT_URI;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivBackPoster)
    ImageView mBackdropPoster;
    @BindView(R.id.ivMoviePoster)
    ImageView mMoviePoster;
    @BindView(R.id.tvOverViewLabel)
    TextView mOverViewLabel;
    @BindView(R.id.tvOverView)
    TextView mOverView;
    @BindView(R.id.tvRating)
    TextView mVoteAverage;
    @BindView(R.id.tvVoteCountLabel)
    TextView mVoteCountLabel;
    @BindView(R.id.tvVoteCount)
    TextView mVoteCount;
    @BindView(R.id.tvReleaseDateLabel)
    TextView mReleaseDateLabel;
    @BindView(R.id.tvReleaseDate)
    TextView mReleaseDate;
    @BindView(R.id.rbVoteAverage)
    RatingBar mRBVoteAverage;
    @BindView(R.id.fab)
    FloatingActionButton btnIsFavorite;
    @BindView(R.id.tvTrailersLabel)
    TextView mTrailersLabel;
    @BindView(R.id.rvTrailers)
    RecyclerView mRVTrailer;
    @BindView(R.id.tvReviewsLabel)
    TextView mReviewsLabel;
    @BindView(R.id.rvReviews)
    RecyclerView mRVReview;

    private List<Trailer> mTrailersList;
    private TrailerAdapter mTrailerAdapter;
    private List<Review> mReviewsList;
    private ReviewAdapter mReviewAdapter;

    private Cursor mCursor;
    private Movie movie;
    private Integer movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        movie = getIntent().getParcelableExtra(getString(R.string.EXTRA_MOVIE));
        if (movie == null) {
            // Movie data unavailable
            closeOnError();
            return;
        }
        movieId = movie.getId();

        if (isFavorites())
            btnIsFavorite.setImageResource(R.drawable.ic_action_favorite);
        else
            btnIsFavorite.setImageResource(R.drawable.ic_action_favorite_border);


        populateUI(movie);
        Picasso.with(this).load(NetworkUtils.POSTER_URL + NetworkUtils.PosterSize.w500 + movie
                .getBackdropPath()).into(mBackdropPoster);
        Picasso.with(this).load(NetworkUtils.POSTER_URL + NetworkUtils.PosterSize.w185 + movie
                .getPosterPath()).into(mMoviePoster);

        if (movie.getTitle() != null)
            setTitle(movie.getTitle());

        btnIsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavorites()) {
                    deleteFromFavorites();
                    btnIsFavorite.setImageResource(R.drawable.ic_action_favorite_border);
                } else {
                    addToFavorites();
                    btnIsFavorite.setImageResource(R.drawable.ic_action_favorite);
                }
                mCursor.close();
            }
        });

    }

    private boolean isFavorites() {
        mCursor = getContentResolver().query(CONTENT_URI,
                new String[]{CLM_MOVIE_ID}, CLM_MOVIE_ID + "=?",
                new String[]{String.valueOf(movieId)}, null);
        return (mCursor != null) && (mCursor.getCount() > 0);
    }

    private void addToFavorites() {

        ContentValues values = new ContentValues();
        values.clear();

        values.put(CLM_MOVIE_ID, movieId);
        values.put(CLM_VOTE_COUNT, movie.getVoteCount());
        values.put(CLM_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(CLM_MOVIE_TITLE, movie.getTitle());
        values.put(CLM_POPULARITY, movie.getPopularity());
        values.put(CLM_OVERVIEW, movie.getOverview());
        values.put(CLM_POSTER_PATH, movie.getPosterPath());
        values.put(CLM_BACKDROP_PATH, movie.getBackdropPath());
        values.put(CLM_RELEASE_DATE, movie.getReleaseDate());

        getContentResolver().insert(CONTENT_URI, values);
    }

    private void deleteFromFavorites() {
        getContentResolver().delete(CONTENT_URI, CLM_MOVIE_ID + "=?", new String[]{String.valueOf(movieId)});
    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie movie) {

        if (movie.getVoteAverage() == null || movie.getVoteAverage().toString().isEmpty()) {
            mVoteAverage.setVisibility(View.GONE);
        } else {
            mVoteAverage.setText(movie.getVoteAverage().toString());
            mRBVoteAverage.setRating(Float.parseFloat(movie.getVoteAverage().toString()));
        }
        if (movie.getVoteCount() == null || movie.getVoteCount().toString().isEmpty()) {
            mVoteCountLabel.setVisibility(View.GONE);
            mVoteCount.setVisibility(View.GONE);
        } else {
            mVoteCount.setText(movie.getVoteCount().toString());
        }
        if (movie.getReleaseDate() == null || movie.getReleaseDate().toString().isEmpty()) {
            mReleaseDateLabel.setVisibility(View.GONE);
            mReleaseDate.setVisibility(View.GONE);
        } else {
            mReleaseDate.setText(movie.getReleaseDate().toString());
        }
        if (movie.getOverview() == null || movie.getOverview().isEmpty()) {
            mOverViewLabel.setVisibility(View.GONE);
            mOverView.setVisibility(View.GONE);
        } else {
            mOverView.setText(movie.getOverview());
        }
        fetchTrailers();
        fetchReviews();
    }


    private void fetchTrailers() {
        mTrailersList = new ArrayList<>();
        mRVTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRVTrailer.setItemAnimator(new DefaultItemAnimator());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<TrailersResponse> trailersResponseCall = apiInterface.getListOfTrailers(movieId, NetworkUtils.API_KEY);
        try {
            if (trailersResponseCall != null) {
                trailersResponseCall.enqueue(new Callback<TrailersResponse>() {

                    @Override
                    public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                        TrailersResponse body = response.body();
                        if (body != null && body.getTrailerResults() != null) {
                            mTrailersList = body.getTrailerResults();
                            mRVTrailer.setAdapter(new TrailerAdapter(getApplicationContext(), mTrailersList));
//                            mTrailerAdapter.notifyDataSetChanged();
                        } else {
                            mTrailersLabel.setVisibility(View.GONE);
                            mRVTrailer.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailersResponse> call, Throwable t) {
                        Log.i("TrailersResponse", "Failed to getTrailersResult");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fetchReviews() {
        mReviewsList = new ArrayList<>();
        mRVReview.setLayoutManager(new LinearLayoutManager(this));
        mRVReview.setItemAnimator(new DefaultItemAnimator());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ReviewsResponse> reviewsResponseCall = apiInterface.getListOfReviews(movieId, NetworkUtils.API_KEY);
        try {
            if (reviewsResponseCall != null) {
                reviewsResponseCall.enqueue(new Callback<ReviewsResponse>() {
                    @Override
                    public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                        ReviewsResponse body = response.body();
                        if (body != null && body.getReviewResults() != null) {
                            mReviewsList = body.getReviewResults();
                            mRVReview.setAdapter(new ReviewAdapter(mReviewsList));
//                            mReviewAdapter.notifyDataSetChanged();
                        } else {
                            mReviewsLabel.setVisibility(View.GONE);
                            mRVReview.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                        Log.i("ReviewsResponse", "Failed to getReviewsResult");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
