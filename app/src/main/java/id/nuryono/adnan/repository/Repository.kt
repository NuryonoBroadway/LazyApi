package id.nuryono.adnan.repository

import android.net.Uri
import id.nuryono.adnan.api.RetrofitInstance
import id.nuryono.adnan.model.Items
import id.nuryono.adnan.model.Post
import retrofit2.Response

class Repository {
    suspend fun mainApi(page: Int): Response<List<Items>> {
        return RetrofitInstance.api.mainApi(page)
    }

    suspend fun customApi(genre: String): Response<List<Items>> {
        return RetrofitInstance.api.customApi(genre)
    }

    suspend fun detailApi(year: Int, month: Int, date: Int, key: String): Response<Post> {
        return RetrofitInstance.api.detailApi(year, month, date, key)
    }
}