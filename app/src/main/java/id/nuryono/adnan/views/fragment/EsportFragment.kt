package id.nuryono.adnan.views.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.nuryono.adnan.adapter.EsportAdapter
import id.nuryono.adnan.databinding.FragmentEsportBinding
import id.nuryono.adnan.repository.Repository
import id.nuryono.adnan.util.hide
import id.nuryono.adnan.util.visible
import id.nuryono.adnan.viewmodel.MainViewModel
import id.nuryono.adnan.viewmodel.MainViewModelFactory
import id.nuryono.adnan.views.detail.DetailActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

class EsportFragment : Fragment() {

    // initialisasi binding
    private var _binding: FragmentEsportBinding? = null
    private val esportBinding get() = _binding
    private lateinit var esportAdapter: EsportAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding di inflate
        _binding = FragmentEsportBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    // jika user berpindah ke fragment lain maka binding pada fragment games akan didestroy
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init adapter
        esportAdapter = EsportAdapter()

        // function yang berfungsi jika user menarik layar dari atas ke bawah
        swipeEsport()

        // function yang berfungsi menstore kegiatan yang bersifat click
        onClick()

        // menampilkan loading pada atas layar
        showLoading()

        // function yang berfungsi menampilkan data dari internet
        showEsport()
    }

    private fun showEsport() {
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
        jadi this merujuk ke FragmentEsport dan .get() mendapatkan model
        untuk menjadikannya aktivitas fragment
         */
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        // reference ke customApi dengan parameter genre
        viewModel.customApi("e-sport")

        // mendapatkan response melalui mutableLiveData
        /* lifeCycleOwner bermaksud mereferensi ke mainActivity dikarenakan main Activity
        ditimpa oleh fragment, maka untuk observe tidak bisa menggunakan this, namum menggunakan
        viewLifeCycleOwner
        * */
        // method observe mengambil object viewLifeCycleOwner
        viewModel.customResponse.observe(viewLifecycleOwner, Observer { response ->
            // jika response success
            if(response.isSuccessful) {
                // hide loading digunakan untuk menghide loading pada atas layar
                hideLoading()
                // setData adapter dengan let bertujuan agar data yang diset bukan nullable
                response.body()?.let { esportAdapter.setData(it) }
            } else {
                hideLoading()
                Log.d("showGames", "showGames: ${response.message()}")
            }
        })

        // set adapter pada rvEsport dengan esportAdapter
        esportBinding?.rvEsport?.adapter = esportAdapter
    }

    private fun onClick() {
        /* pada gamesAdapter terdapat function click
       * yang bertujuan untuk listen setiap click pada setiap item yang diloop
       * pada onClick dibawah terdapat esport dan position
       * disini pada onCLick melisten pada position yang diclick, sama halnya
       * dengan for each akan mereferensikan index
       * */
        esportAdapter.onClick { esport, position ->
            // jika menggunakan fragment maka untuk startActivity didahului dengan context
            context?.startActivity<DetailActivity>(
                // pada DetailActivity terdapat companion object digunakan untuk passing data ke activity lain
                DetailActivity.KEY_ITEMS to esport,
                DetailActivity.KEY_POSITION to position
            )
        }
    }

    // function jika user swipe atas layar sehingga activity akan terefresh
    private fun swipeEsport() {
        esportBinding?.swipeEsport?.setOnRefreshListener {
            showEsport()
        }
    }

    // function yang bertujuan agas swipeGames id pada fragment akan muncul
    private fun showLoading() {
        esportBinding?.swipeEsport?.visible()
    }

    // function yang bertujuan agas swipeGames id pada fragment akan hilang
    private fun hideLoading() {
        esportBinding?.swipeEsport?.hide()
    }
}