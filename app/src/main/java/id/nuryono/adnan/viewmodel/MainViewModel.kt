package id.nuryono.adnan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.nuryono.adnan.model.Items
import id.nuryono.adnan.model.Post
import id.nuryono.adnan.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private var repository: Repository): ViewModel() {
    /*
    init response setiap function yang dipanggil
    dengan MutableLiveData, sehingga bisa mendapatkan response
    ketika dipanggil
     */
    val myResponse: MutableLiveData<Response<List<Items>>> = MutableLiveData()
    val customResponse: MutableLiveData<Response<List<Items>>> = MutableLiveData()
    val detailResponse: MutableLiveData<Response<Post>> = MutableLiveData()

    // function pada mainApi yang passing paramater untuk mendaptkan data berdasarkan page
    fun mainApi(page: Int) {

        // koroutin yang berfungsi sebagai asyncron
        viewModelScope.launch {
            val response = repository.mainApi(page)
            // response di init ke myResponse
            myResponse.value = response
        }
    }

    // function pada customApi yang passing genre dari fragment atau filter berdasarkan tag artikel
    fun customApi(genre: String) {
        // koroutin yang befungsi sebagai asyncron
        viewModelScope.launch {
            val response = repository.customApi(genre)
            // response di init ke customResponse
            customResponse.value = response
        }
    }

    // function pada detailApi passing beberapa paramater dan return json detail berdasarkan key
    fun detailApi(year: Int, month: Int, date: Int, key: String) {
        // koroutin yang befungsi sebagai asyncron
        viewModelScope.launch {
            val response = repository.detailApi(year, month, date, key)
            // response di init ke detailResponse
            detailResponse.value = response
        }
    }
}