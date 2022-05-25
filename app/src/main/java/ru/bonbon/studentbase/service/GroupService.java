package ru.bonbon.studentbase.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import ru.bonbon.studentbase.entity.Group;

public interface GroupService {
    @GET("group/get-all")
    Call<List<Group>> getAll();

    @GET("group/get-by")
    Call<List<Group>> getByFacultyId(@Query("id_faculty") int id);

    @GET("group/get")
    Call<Group> get(@Query("id") int id);

    @POST("group/create")
    Call<Group> create(@Body Group group);

    @DELETE("group/delete")
    Call<Void> delete(@Query("id") int id);

    @PUT("group/update")
    Call<Void> update(@Body Group group);
}
