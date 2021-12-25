package id.nuryono.adnan.api

import android.net.Uri
import id.nuryono.adnan.model.Items
import id.nuryono.adnan.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    // get api dengan query maka akan tampil seperti "api/games?page=1" dane return response list Items
    @GET("api/games")
    suspend fun mainApi(@Query("page") page: Int): Response<List<Items>>

    // get api dengan path genre untuk menentukan catch api yang spesifik dane return response list Items
    @GET("api/games/{genre}?page=1")
    suspend fun customApi(@Path("genre") genre: String): Response<List<Items>>

    // get api detail menentukan path year, month, date, dan key api dan mereturn single Post detail
    @GET("api/detail/{year}/{month}/{date}/{key}")
    suspend fun detailApi(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Path("date") date: Int,
        @Path("key") key: String
    ): Response<Post>
}