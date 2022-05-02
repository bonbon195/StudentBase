package ru.bonbon.sdudentdatabase.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import ru.bonbon.sdudentdatabase.entity.Student;


public interface StudentService {
    @GET("student/get-all")
    Call<List<Student>> getAll();

    @GET("student/get-by")
    Call<List<Student>> getByGroupId(@Query("id_group") int id);

    @GET("student/get")
    Call<Student> get(@Query("id") int id);

    @POST("student/create")
    Call<Student> create(@Body Student student);

    @DELETE("student/delete")
    Call<Void> delete(@Query("id") int id);

    @PUT("student/update")
    Call<Void> update(@Body Student student);
}
