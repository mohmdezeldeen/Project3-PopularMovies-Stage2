package ezz.abdulaziz.project3_popularmovies_stage2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ezz.abdulaziz.project3_popularmovies_stage2.R;
import ezz.abdulaziz.project3_popularmovies_stage2.activity.DetailsActivity;
import ezz.abdulaziz.project3_popularmovies_stage2.model.Movie;
import ezz.abdulaziz.project3_popularmovies_stage2.utils.NetworkUtils;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context mContext;
    private List<Movie> mMoviesList;

    public MovieAdapter(Context mContext, List<Movie> mMoviesList) {
        this.mContext = mContext;
        this.mMoviesList = mMoviesList;
    }


    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_grid_item,
                viewGroup, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
        Movie movie = mMoviesList.get(position);
        Picasso.with(mContext).load(NetworkUtils.POSTER_URL + NetworkUtils.PosterSize.w185 +
                movie.getPosterPath()).centerCrop().fit().into(holder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        return (mMoviesList == null) ? 0 : mMoviesList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivMoviePoster)
        ImageView mMoviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick()
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = mMoviesList.get(position);
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(mContext.getString(R.string.EXTRA_MOVIE), movie);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }
    }

}
