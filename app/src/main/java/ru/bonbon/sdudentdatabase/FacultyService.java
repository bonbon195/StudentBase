package ru.bonbon.sdudentdatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.bonbon.sdudentdatabase.entity.Faculty;

public interface FacultyService {
    @Headers({"Accept: application/json"})
    @GET("faculty/get-all")
    Call<List<Faculty>> getAll();

    @GET("faculty/{id}")
    Call<Faculty> get(@Path("id") int id);

    @POST("faculty/create")
    Call<Faculty> create(@Body Faculty faculty);
}
