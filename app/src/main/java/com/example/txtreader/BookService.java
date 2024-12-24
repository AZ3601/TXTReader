package com.example.txtreader;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Multipart;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;
 //okhttp3是否要添加到xml中？
public interface BookService {
    @GET("/get_books")
    Call<List<Book>> getBooks();

    @GET("/get_book/{book_id}")
    Call<Book> getBook(@Path("book_id") int bookId);

    @GET("/get_book_content/{book_id}")
    Call<ResponseBody> getBookContent(@Path("book_id") int bookId);

    @GET("/get_user_bookshelf/{user_id}")
    Call<List<Book>> getUserBookshelf(@Path("user_id") int userId);

    @FormUrlEncoded
    @POST("/register")
    Call<Void> registerUser(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/login")
    Call<LoginResponse> login(@Field("username") String username, @Field("password") String password);
    @GET("/books")
    Call<List<Book>> getAllBooks();
    @Multipart
    @POST("/uploadBook")
    Call<Void> uploadBook(
            @Part("title") RequestBody title,
            @Part("author") RequestBody author,
            @Part MultipartBody.Part coverImage,
            @Part MultipartBody.Part contentFile
    );
    @FormUrlEncoded
    @POST("/add_to_bookshelf")
    Call<Void> addToBookshelf(@Field("user_id") int userId, @Field("book_id") int bookId);
    @FormUrlEncoded
    @POST("/remove_from_bookshelf")
    Call<Void> removeFromBookshelf(@Field("user_id") int userId, @Field("book_id") int bookId);
}