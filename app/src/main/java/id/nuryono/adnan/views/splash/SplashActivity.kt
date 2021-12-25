package id.nuryono.adnan.views.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import id.nuryono.adnan.R
import id.nuryono.adnan.views.main.MainActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        delayAndGoToMain()
    }


    // delay untuk menuju main_activity sebesar 1,5 detik
    private fun delayAndGoToMain() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity<MainActivity>()
        }, 1500)
    }
}