package ezz.abdulaziz.project2_popularmovies_stage2.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ezz.abdulaziz.project2_popularmovies_stage2.R;
import ezz.abdulaziz.project2_popularmovies_stage2.model.Trailer;
import ezz.abdulaziz.project2_popularmovies_stage2.utils.NetworkUtils;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private final Context mContext;
    private final List<Trailer> mTrailersList;

    public TrailerAdapter(Context mContext, List<Trailer> mTrailersList) {
        this.mContext = mContext;
        this.mTrailersList = mTrailersList;
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_trailer_item, viewGroup, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailersList.get(position);
        holder.mTrailerName.setText(trailer.getName());
        Picasso.with(mContext).load(NetworkUtils.TRAILER_THUMBNAIL_URL + trailer.getKey() +
                NetworkUtils.TRAILER_THUMBNAIL_URL2).into(holder.mTrailerThumbnail);
    }

    @Override
    public int getItemCount() {
        return (mTrailersList == null) ? 0 : mTrailersList.size();
    }


    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTrailerName)
        TextView mTrailerName;
        @BindView(R.id.ivTrailerThumbnail)
        ImageView mTrailerThumbnail;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick()
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                String trailerKey = mTrailersList.get(position).getKey();
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(NetworkUtils.YOUTUBE_WEB_URL + trailerKey));
                Intent appIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(NetworkUtils.YOUTUBE_APP_URL + trailerKey));
                try {
                    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    appIntent.putExtra(mContext.getString(R.string.TRAILER_ID_KEY), trailerKey);
                    mContext.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    webIntent.putExtra(mContext.getString(R.string.TRAILER_ID_KEY), trailerKey);
                    mContext.startActivity(webIntent);
                }
            }
        }

    }
}

