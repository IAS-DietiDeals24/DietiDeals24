package com.iasdietideals24.dietideals24.utilities

import com.iasdietideals24.dietideals24.controller.ControllerAccesso.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {

    @GET("account")
    fun accedi(@Query("email") userEmail: String, @Query("password") userPassword: String): Call<User>

}
