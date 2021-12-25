package id.nuryono.adnan.views.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import id.nuryono.adnan.R
import id.nuryono.adnan.databinding.ActivityMainBinding
import id.nuryono.adnan.views.fragment.EsportFragment
import id.nuryono.adnan.views.fragment.GameReviewFragment
import id.nuryono.adnan.views.fragment.GamesFragment
import id.nuryono.adnan.views.fragment.LazyTalkFragment

class MainActivity : AppCompatActivity() {

    // set binding main
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inflate binding
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // fungsi untuk inisialisai
        init()
    }

    private fun init() {

        /*
        * set btm navigation main sesuai id pada layout main dan set item setiap click
        * pada id di setiap item pada main_menu.xml
        *
        * jika views setiap item di click maka akan meneruskan ke openFragment
        * */
        mainBinding.btmNavigationMain.setOnItemSelectedListener { id ->
            when(id) {
                R.id.action_games -> openFragment(GamesFragment())
                R.id.action_esport -> openFragment(EsportFragment())
                R.id.action_lazy_talk -> openFragment(LazyTalkFragment())
                R.id.action_game_review -> openFragment(GameReviewFragment())
            }
        }

        // default jika tidak ada id yang di click
        openHomeFragment()
    }


    override fun onBackPressed() {
        super.onBackPressed()

        /*
        * mendapatkan selected id. jika id berada pada action_games maka aplikasi akan difinish
        * namun, jika berada di id lain maka btmNavigation akan set item ke action_games
        * */
        val selectedItemId = mainBinding.btmNavigationMain.getSelectedItemId()
        if (selectedItemId == R.id.action_games) {
            finishAffinity()
        } else {
            openHomeFragment()
        }
    }

    private fun openFragment(fragment: Fragment) {
        /*
        * digunakan untuk open fragment sehingga layout main bisa memuat fragment
        *
        * dengan mereplace id dari frame_main dengan fragment
        * */

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    // default fragment yang terbuka pertama kali
    private fun openHomeFragment() {
        mainBinding.btmNavigationMain.setItemSelected(R.id.action_games)
    }


}