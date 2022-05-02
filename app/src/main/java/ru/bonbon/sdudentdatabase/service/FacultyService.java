package ru.bonbon.sdudentdatabase.service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.bonbon.sdudentdatabase.entity.Faculty;

public interface FacultyService {
    @Headers({"Accept: application/json"})
    @GET("faculty/get-all")
    Call<List<Faculty>> getAll();

    @Headers({"Accept: application/json"})
    @GET("faculty/get")
    Call<Faculty> get(@Query("id") int id);

    @POST("faculty/create")
    Call<Void> create(@Body Faculty faculty);

    @DELETE("faculty/delete")
    Call<Void> delete(@Query("id") int id);

    @PUT("faculty/update")
    Call<Void> update(@Body Faculty faculty);
}
