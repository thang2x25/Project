package com.example.a21029381_nguyenmaiducthang;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("products")
    Call<List<Product>> getProducts();

    @POST("customers/login")
    Call<Customer> loginCustomer(@Body Customer customer);

    @POST("customers")
    Call<Customer> registerCustomer(@Body Customer customer);

    @POST("products")
    Call<Product> sendPost(@Body Product product);

    @POST("orders")
    Call<Order> sendOrder(@Body Order order);
}
