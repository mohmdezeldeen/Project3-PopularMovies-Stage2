package ezz.abdulaziz.project3_popularmovies_stage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse {

    @SerializedName("results")
    private List<Review> results;

    public ReviewsResponse(List<Review> results) {
        this.results = results;
    }

    public List<Review> getReviewResults() {
        return results;
    }
}
