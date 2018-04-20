package ezz.abdulaziz.project2_popularmovies_stage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResponse {

    @SerializedName("results")
    private List<Trailer> results;

    public TrailersResponse(List<Trailer> results) {
        this.results = results;
    }

    public List<Trailer> getTrailerResults() {
        return results;
    }
}
