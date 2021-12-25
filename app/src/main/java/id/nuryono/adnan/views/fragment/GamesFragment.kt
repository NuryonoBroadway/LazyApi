package id.nuryono.adnan.views.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.nuryono.adnan.adapter.GamesAdapter
import id.nuryono.adnan.databinding.FragmentGamesBinding
import id.nuryono.adnan.repository.Repository
import id.nuryono.adnan.util.gone
import id.nuryono.adnan.util.hide
import id.nuryono.adnan.util.visible
import id.nuryono.adnan.viewmodel.MainViewModel
import id.nuryono.adnan.viewmodel.MainViewModelFactory
import id.nuryono.adnan.views.detail.DetailActivity
import org.jetbrains.anko.startActivity

class GamesFragment : Fragment() {

    // TAG digunakan untuk log
    private var TAG = "GamesFragment"

    // initialisasi binding
    private var _binding: FragmentGamesBinding? = null
    private val gamesBinding get() = _binding

    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding di inflate
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    // jika user berpindah ke fragment lain maka binding pada fragment games akan didestroy
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init adapter
        gamesAdapter = GamesAdapter()

        // function yang berfungsi jika user menarik layar dari atas ke bawah
        swipeRefreshGames()

        // function yang berfungsi menstore kegiatan yang bersifat click
        onClick()

        // menampilkan loading pada atas layar
        showLoading()

        // function yang berfungsi menampilkan data dari internet
        showGames()
    }

    private fun showGames(page: Int = 1) {
        // initialisasi repository
        val repository = Repository()

        // initialisasi viewModelFactory
        val viewModelFactory = MainViewModelFactory(repository)

        /* init viewModel dengan viewModelProvider, untuk mendapatkan MainViewModel yang berisikan
         * function api
         * */
        /*
       viewModelProvider() mengembalikan ViewModel yang ada (jika ada) atau membuat
       yang baru jika tidak ada akan Membuat instance ViewModel terkait
       dengan cakupan yang diberikan
        */
        /*
        jadi this merujuk ke FragmentGames dan .get() mendapatkan model
        untuk menjadikannya aktivitas fragment
         */
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        // reference ke mainApi dengan parameter page == 1
        viewModel.mainApi(page)

        // mendapatkan response melalui mutableLiveData
        /* lifeCycleOwner bermaksud mereferensi ke mainActivity dikarenakan main Activity
        ditimpa oleh fragment, maka untuk observe tidak bisa menggunakan this, namum menggunakan
        viewLifeCycleOwner
        * */
        // method observe mengambil object viewLifeCycleOwner
        viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
            // jika response success
            if(response.isSuccessful) {
                Log.d(TAG, "showGames: ${response.raw()}")
                // setData adapter dengan let bertujuan agar data yang diset bukan nullable
                response.body()?.let { gamesAdapter.setData(it) }

                // hide loading digunakan untuk menghide loading pada atas layar
                hideLoading()
            } else {
                Log.d("showGames", "showGames: ${response.message()}")
                hideLoading()
            }
        })

        // set adapter pada rvGames dengan gamesAdapter
        gamesBinding?.rvGames?.adapter = gamesAdapter
    }

    private fun onClick() {
        /* pada gamesAdapter terdapat function click
        * yang bertujuan untuk listen setiap click pada setiap item yang diloop
        * pada onClick dibawah terdapat games dan position
        * disini pada onCLick melisten pada position yang diclick, sama halnya
        * dengan for each akan mereferensikan index
        * */
        gamesAdapter.onClick { games, position ->
            // jika menggunakan fragment maka untuk startActivity didahului dengan context
            context?.startActivity<DetailActivity>(
                // pada DetailActivity terdapat companion object digunakan untuk passing data ke activity lain
                DetailActivity.KEY_ITEMS to games,
                DetailActivity.KEY_POSITION to position
            )
        }
    }

    // function jika user swipe atas layar sehingga activity akan terefresh
    private fun swipeRefreshGames() {
        gamesBinding?.swipeGames?.setOnRefreshListener {
            /*
            * set random dari 1 sampai 100 karena api yang saya gunakan hanya berisi hingga 100 page
            * */
            val random = (1..100).random()
            showGames(random)
        }
    }

    // function yang bertujuan agas swipeGames id pada fragment akan muncul dan id pada rvGames akan hilang
    private fun showLoading() {
        gamesBinding?.swipeGames?.visible()
        gamesBinding?.rvGames?.gone()
    }

    // function yang bertujuan agas swipeGames id pada fragment akan hilang dan id pada rvGames akan muncul
    private fun hideLoading() {
        gamesBinding?.swipeGames?.hide()
        gamesBinding?.rvGames?.visible()
    }
}