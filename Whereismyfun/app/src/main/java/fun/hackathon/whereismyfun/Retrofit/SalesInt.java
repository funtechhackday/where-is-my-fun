package fun.hackathon.whereismyfun.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SalesInt {
    @GET("/{lat}/{lng}")
    Call<Company> getCompanys(@Path("lat") double lat, @Path("lng") double lng);
}
