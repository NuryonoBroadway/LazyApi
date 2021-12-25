package id.nuryono.adnan.views.detail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.nuryono.adnan.databinding.ActivityDetailBinding
import id.nuryono.adnan.model.Items
import id.nuryono.adnan.model.Results
import id.nuryono.adnan.repository.Repository
import id.nuryono.adnan.util.hide
import id.nuryono.adnan.util.visible
import id.nuryono.adnan.viewmodel.MainViewModel
import id.nuryono.adnan.viewmodel.MainViewModelFactory

class DetailActivity : AppCompatActivity() {

    // item untuk passing data dari activity lain
    companion object {
        const val KEY_ITEMS = "key_items"
        const val KEY_POSITION = "key_position"
    }

    // init tag untuk log
    private val TAG = "DetailActivity"

    // init binding
    private lateinit var detailBinding: ActivityDetailBinding

    // init items dengan type List
    private var items: List<Items>? = null

    // init position
    private var position = 0

    // init viewModel
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set binding
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        // aktivitas refresh
        swipeGameReview()

        // init action bar
        init()

        // aktivitas menampilkan loading saat get data
        showLoading()

        // function untuk getData dan menampilkan data ke views
        getData()

        // activitas untuk kegiatan click
        onCLick()
    }

    private fun getData() {

        // jika intent tidak null, maka data intent yang tadi di passing melalui fragment dapat di ambil
        if (intent != null) {

            /* pada model terdapat parceable yang bertujuan agar
            lebih mudah progam untuk mengambil data yang spesifik
             */
            items = intent.getParcelableArrayListExtra(KEY_ITEMS)

            // mengambil return position dari recycleViews denagn default value 0
            position = intent.getIntExtra(KEY_POSITION, 0)

            // cek apakah items nullable
            items?.let {
                // mendapatkan item berdasarkan position
                val item = it[position]
                Log.d("ItemList", "getData: ${item.key.split("/")}")

                // init repository
                val repository = Repository()

                // init viewModelFactory yang digunakan untuk mendapatkan class dari MainViewModel
                val viewModelFactory = MainViewModelFactory(repository)

                // result ini akan memisahkan item menjadi array memakai split dengan syarat split adalah /
                val result = item.key.split("/")

                /*
                viewModelProvider() mengembalikan ViewModel yang ada (jika ada) atau membuat
                yang baru jika tidak ada akan Membuat instance ViewModel terkait
                dengan cakupan yang diberikan
                 */

                /*
                jadi this merujuk ke detailActivity dan .get() mendapatkan model
                untuk menjadikannya aktivitas
                 */
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

                // mendapatkan detail dari api dengan passing beberapa parameter
                viewModel.detailApi(result[0].toInt(), result[1].toInt(), result[2].toInt(), result[3])

                // viewModel untuk observe menggunakan this karena activity ini tidak ditimpa oleh fragment apapun
                viewModel.detailResponse.observe(this, Observer { response ->
                    if(response.isSuccessful) {
                        hideLoading()
                        // menampikan data pada views
                        response.body()?.let { it1 -> initView(it1.results) }
                    } else {
                        hideLoading()
                        Log.d("showGames", "showGames: ${response.message()}")
                    }
                })
            }
        }
    }

    private fun initView(result: Results) {

        // menampilkan data sesuai id pada views
        detailBinding.tvTitle.text = result.title
        detailBinding.tvDate.text = result.date
        detailBinding.tvAuthor.text = result.author

        // membuat string kosong
        var content: String? = null

        /* forEach content yang bersifat list<Any>
        dan diberi kondisi jika item tidak terdapat jpg, png, dan tidak null,
        maka content akan terisi oleh item
         */

        result.content.forEach { item ->
            if(!item.toString().contains("jpg") && !item.toString().contains("png") && item.toString() != "null") {
                content += item.toString().trim() + "\n\n"
            }
        }

        // menampilkan content dengan replace string null menjadi "" dan trim agar whitespace tidak ikut terender
        detailBinding.tvContent.text = content.toString().replace("null", "").trim()
        Log.d(TAG, "initView: $content")
    }


    // navigation untuk kembali ke Main, maka detailActivity akan didestroy
    private fun onCLick() {
        detailBinding.tbDetail.setNavigationOnClickListener {
            finish()
        }
    }

    // init ActionBar
    private fun init() {
        setSupportActionBar(detailBinding.tbDetail)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun swipeGameReview() {
        detailBinding.swipeDetail.setOnRefreshListener {
            getData()
        }
    }

    private fun showLoading() {
        detailBinding.swipeDetail.visible()
    }

    private fun hideLoading() {
        detailBinding.swipeDetail.hide()
    }


}