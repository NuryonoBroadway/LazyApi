package id.nuryono.adnan.views.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.nuryono.adnan.adapter.GameReviewAdapter
import id.nuryono.adnan.databinding.FragmentGameReviewBinding
import id.nuryono.adnan.repository.Repository
import id.nuryono.adnan.util.hide
import id.nuryono.adnan.util.visible
import id.nuryono.adnan.viewmodel.MainViewModel
import id.nuryono.adnan.viewmodel.MainViewModelFactory
import id.nuryono.adnan.views.detail.DetailActivity
import org.jetbrains.anko.startActivity

class GameReviewFragment : Fragment() {

    // initialisasi binding
    private var _binding: FragmentGameReviewBinding? = null
    private val gameReviewBinding get() = _binding
    private lateinit var gameReviewAdapter: GameReviewAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding di inflate
        _binding = FragmentGameReviewBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    // destroy fragment jika user berpindah fragment
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init adapter
        gameReviewAdapter = GameReviewAdapter()
        swipeGameReview()

        onClick()

        showLoading()
        showGameReview()
    }

    private fun showGameReview() {
        // init repository
        val repository = Repository()
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
        jadi this merujuk ke FragmentGameReview dan .get() mendapatkan model
        untuk menjadikannya aktivitas fragment
         */
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.customApi("review")

        // mendapatkan response melalui mutableLiveData
        /* lifeCycleOwner bermaksud mereferensi ke mainActivity dikarenakan main Activity
        ditimpa oleh fragment, maka untuk observe tidak bisa menggunakan this, namum menggunakan
        viewLifeCycleOwner
        * */
        // method observe mengambil object viewLifeCycleOwner
        viewModel.customResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response.isSuccessful) {
                hideLoading()

                // setData adapter
                response.body()?.let { gameReviewAdapter.setData(it) }
            } else {
                hideLoading()
                Log.d("showGames", "showGames: ${response.message()}")
            }
        })

        gameReviewBinding?.rvGameReview?.adapter = gameReviewAdapter
    }

    private fun onClick() {
        gameReviewAdapter.onClick { game, position ->
            context?.startActivity<DetailActivity>(
                DetailActivity.KEY_ITEMS to game,
                DetailActivity.KEY_POSITION to position
            )
        }
    }

    private fun swipeGameReview() {
        gameReviewBinding?.swipeGameReview?.setOnRefreshListener {
           showGameReview()
        }
    }

    private fun showLoading() {
        gameReviewBinding?.swipeGameReview?.visible()
    }

    private fun hideLoading() {
        gameReviewBinding?.swipeGameReview?.hide()
    }

}