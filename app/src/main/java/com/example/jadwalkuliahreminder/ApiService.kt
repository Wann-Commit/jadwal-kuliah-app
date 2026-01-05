package com.example.jadwalkuliahreminder

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("api_jadwal.php")
    suspend fun getJadwal(@Query("user_id") userId: Int = 1): Response<ApiResponse>

    @POST("api_jadwal.php")
    suspend fun addJadwal(@Body jadwal: MataKuliah): Response<ApiResponse>

    @PUT("api_jadwal.php")
    suspend fun updateJadwal(@Body jadwal: MataKuliah): Response<ApiResponse>

    @HTTP(method = "DELETE", path = "api_jadwal.php", hasBody = true)
    suspend fun deleteJadwal(@Body data: Map<String, Int>): Response<ApiResponse>
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2/api_jadwal/" // Untuk emulator
    // private const val BASE_URL = "http://192.168.1.100/api_jadwal/" // Untuk device fisik

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}