package ezz.abdulaziz.project2_popularmovies_stage2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ezz.abdulaziz.project2_popularmovies_stage2.R;
import ezz.abdulaziz.project2_popularmovies_stage2.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private final List<Review> mReviewsList;

    public ReviewAdapter(List<Review> mReviewsList) {
        this.mReviewsList = mReviewsList;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvAuthor)
        TextView mTVAuthor;
        @BindView(R.id.tvContent)
        TextView mTVContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_review_item, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {
        Review review = mReviewsList.get(position);
        holder.mTVAuthor.setText(review.getAuthor());
        holder.mTVContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return (mReviewsList == null) ? 0 : mReviewsList.size();
    }
}
