package id.nuryono.adnan.views.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.nuryono.adnan.adapter.LazyTalkAdapter
import id.nuryono.adnan.databinding.FragmentLazyTalkBinding
import id.nuryono.adnan.repository.Repository
import id.nuryono.adnan.util.hide
import id.nuryono.adnan.util.visible
import id.nuryono.adnan.viewmodel.MainViewModel
import id.nuryono.adnan.viewmodel.MainViewModelFactory
import id.nuryono.adnan.views.detail.DetailActivity
import org.jetbrains.anko.startActivity

class LazyTalkFragment : Fragment() {

    // initialisasi binding
    private var _binding: FragmentLazyTalkBinding? = null
    private val lazyTalkBinding get() = _binding
    private lateinit var lazyTalkAdapter: LazyTalkAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding di inflate
        _binding = FragmentLazyTalkBinding.inflate(inflater, container, false)
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
        lazyTalkAdapter = LazyTalkAdapter()
        swipeLazyTalk()

        onClick()

        showLoading()
        showLazyTalk()
    }

    private fun showLazyTalk() {
        // initialisasi repository
        val repository = Repository()

        // init viewModelFactory
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
        jadi this merujuk ke FragmentLazyTalk dan .get() mendapatkan model
        untuk menjadikannya aktivitas fragment
         */

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        // referensi customApi dengan pass parameter
        viewModel.customApi("lazy-talk")

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
                response.body()?.let { lazyTalkAdapter.setData(it) }
            } else {
                hideLoading()
                Log.d("showGames", "showGames: ${response.message()}")
            }
        })

        // set adapter pada rvLazyTalk
        lazyTalkBinding?.rvLazyTalk?.adapter = lazyTalkAdapter
    }

    private fun onClick() {
        /* disini saya menggunakan nama lazy, games atau esport namun kasus ini
        * data yang distore akan return data yang sama namun nama panggilannya saya sesuaikan
        * dengan nama fragment
        * */
        lazyTalkAdapter.onClick { lazy, position ->
            context?.startActivity<DetailActivity>(
                DetailActivity.KEY_ITEMS to lazy,
                DetailActivity.KEY_POSITION to position
            )
        }
    }

    private fun swipeLazyTalk() {
        lazyTalkBinding?.swipeLazyTalk?.setOnRefreshListener {
            showLazyTalk()
        }
    }

    private fun showLoading() {
        lazyTalkBinding?.swipeLazyTalk?.visible()
    }

    private fun hideLoading() {
        lazyTalkBinding?.swipeLazyTalk?.hide()
    }


}