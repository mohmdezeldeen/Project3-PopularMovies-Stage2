package ezz.abdulaziz.project2_popularmovies_stage2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ezz.abdulaziz.project2_popularmovies_stage2.R;
import ezz.abdulaziz.project2_popularmovies_stage2.model.Movie;
import ezz.abdulaziz.project2_popularmovies_stage2.utils.NetworkUtils;

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
    FloatingActionButton isFavorite;

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

        Movie movie = getIntent().getParcelableExtra(getString(R.string.EXTRA_MOVIE));
        if (movie == null) {
            // Movie data unavailable
            closeOnError();
            return;
        }

        populateUI(movie);
        Picasso.with(this).load(NetworkUtils.POSTER_URL + NetworkUtils.PosterSize.w500 + movie
                .getBackdropPath()).into(mBackdropPoster);
        Picasso.with(this).load(NetworkUtils.POSTER_URL + NetworkUtils.PosterSize.w185 + movie
                .getPosterPath()).into(mMoviePoster);

        if (movie.getTitle() != null)
            setTitle(movie.getTitle());

        isFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie movie) {

        if (movie.getVoteAverage() == null ||  movie.getVoteAverage().toString().isEmpty()) {
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
        if (movie.getReleaseDate() == null||movie.getReleaseDate().toString().isEmpty() ) {
            mReleaseDateLabel.setVisibility(View.GONE);
            mReleaseDate.setVisibility(View.GONE);
        } else {
            mReleaseDate.setText(movie.getReleaseDate().toString());
        }
        if (movie.getOverview() == null || movie.getOverview().isEmpty() ) {
            mOverViewLabel.setVisibility(View.GONE);
            mOverView.setVisibility(View.GONE);
        } else {
            mOverView.setText(movie.getOverview());
        }
    }

}
