package fun.hackathon.whereismyfun.Retrofit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Companys {
    @GET("/{lat}/{lng}")
    Call<Company> getCompanys(@Path("lat") double lat, @Path("lng") double lng);
}
