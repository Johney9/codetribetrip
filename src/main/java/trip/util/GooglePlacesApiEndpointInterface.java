package trip.util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import trip.model.google.GoogleLocation;

public interface GooglePlacesApiEndpointInterface {
    String BASE_URL = "https://maps.googleapis.com";

    @GET("/maps/api/place/textsearch/json")
    Call<GoogleLocation> getGoogleLocationResults(@Query("query") String query, @Query("key") String key);
}
