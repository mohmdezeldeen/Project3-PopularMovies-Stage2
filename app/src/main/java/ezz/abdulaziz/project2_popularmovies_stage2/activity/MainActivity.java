package ezz.abdulaziz.project2_popularmovies_stage2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ezz.abdulaziz.project2_popularmovies_stage2.R;
import ezz.abdulaziz.project2_popularmovies_stage2.adapter.MovieAdapter;
import ezz.abdulaziz.project2_popularmovies_stage2.database.DBHelper;
import ezz.abdulaziz.project2_popularmovies_stage2.model.Movie;
import ezz.abdulaziz.project2_popularmovies_stage2.model.MoviesResponse;
import ezz.abdulaziz.project2_popularmovies_stage2.rest.ApiClient;
import ezz.abdulaziz.project2_popularmovies_stage2.rest.ApiInterface;
import ezz.abdulaziz.project2_popularmovies_stage2.utils.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.BaseColumns._ID;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_BACKDROP_PATH;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_MOVIE_ID;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_MOVIE_TITLE;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_OVERVIEW;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_POPULARITY;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_POSTER_PATH;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_RELEASE_DATE;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_VOTE_AVERAGE;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_VOTE_COUNT;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CONTENT_URI;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private List<Movie> mMovieList;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private MovieAdapter mMovieAdapter;
    private static String SHOW_TYPE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SHOW_TYPE = preferences.getString(getString(R.string.pref_sort_type), String.valueOf(NetworkUtils.ShowType.Popular));
        preferences.registerOnSharedPreferenceChangeListener(this);

        setTitle(getString(R.string.title_activity_main_popular));
        mMovieList = new ArrayList<>();

        checkConnection();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        mMovieAdapter = new MovieAdapter(this, mMovieList);
        mRecyclerView.setAdapter(mMovieAdapter);
        mMovieAdapter.notifyDataSetChanged();
    }

    private void checkConnection() {
        if (!NetworkUtils.isConnectionAvailable(this)) {

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(MainActivity.this);
            }
            builder.setMessage(R.string.error_internet_connection)
                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkConnection();
                        }
                    })
                    .setNegativeButton(R.string.show_favorite, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            fetchMoviesFromDB();
                        }
                    })
                    .show();
        } else {
            fetchMoviesByShowType();
        }
    }

    private void fetchMoviesFromDB() {
        setTitle(getString(R.string.title_activity_main_favorite));
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        mMovieList.clear();

        String[] projection = {
                _ID,
                CLM_MOVIE_ID,
                CLM_VOTE_COUNT,
                CLM_VOTE_AVERAGE,
                CLM_POPULARITY,
                CLM_MOVIE_TITLE,
                CLM_OVERVIEW,
                CLM_POSTER_PATH,
                CLM_BACKDROP_PATH,
                CLM_RELEASE_DATE,
        };
        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, null, null,
                CLM_POPULARITY + " ASC");

        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Movie movie = new Movie(
                        cursor.getInt(cursor.getColumnIndex(CLM_MOVIE_ID)),
                        cursor.getInt(cursor.getColumnIndex(CLM_VOTE_COUNT)),
                        cursor.getDouble(cursor.getColumnIndex(CLM_VOTE_AVERAGE)),
                        cursor.getDouble(cursor.getColumnIndex(CLM_POPULARITY)),
                        cursor.getString(cursor.getColumnIndex(CLM_MOVIE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(CLM_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(CLM_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndex(CLM_BACKDROP_PATH)),
                        cursor.getString(cursor.getColumnIndex(CLM_RELEASE_DATE))
                );
                mMovieList.add(movie);
                cursor.moveToNext();
            }
            cursor.close();
        }
        dbHelper.close();
    }

    private void fetchMoviesByShowType() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> moviesResponseCall = null;
        if (SHOW_TYPE.equals(String.valueOf(NetworkUtils.ShowType.Popular))) {
            setTitle(getString(R.string.title_activity_main_popular));
            moviesResponseCall = apiInterface.getPopularMovies(NetworkUtils.API_KEY);
        } else if (SHOW_TYPE.equals(String.valueOf(NetworkUtils.ShowType.TopRated))) {
            setTitle(getString(R.string.title_activity_main_top_rated));
            moviesResponseCall = apiInterface.getTopRatedMovies(NetworkUtils.API_KEY);
        } else {
            fetchMoviesFromDB();
        }

        if (moviesResponseCall != null) {
            moviesResponseCall.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    MoviesResponse body = response.body();
                    if (body != null && body.getResults() != null)
                        mMovieList = body.getResults();
                    mRecyclerView.setAdapter(new MovieAdapter(getApplicationContext(), mMovieList));
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.i("MoviesResponse", "Failed to getMoviesResult");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_type)))
            SHOW_TYPE = sharedPreferences.getString(key, String.valueOf(NetworkUtils.ShowType.Popular));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
