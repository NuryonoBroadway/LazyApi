package id.nuryono.adnan.api

import id.nuryono.adnan.util.constant.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    /*
    Inisialisasi by lazy { ... } adalah
    thread-safe secara default dan menjamin bahwa
    penginisialisasi dipanggil paling banyak satu kali
    (tetapi ini dapat diubah dengan menggunakan lazy overload lainnya)
    */
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    // inisialisi api dengan type ApiInterface, digunakan untuk menyambungkan retrofit ke interface
    val api: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }

}