package id.nuryono.adnan.util

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

// jika function ini dipanggil maka SwipeRefreshLayout akan ditampilkan
fun SwipeRefreshLayout.visible() {
    isRefreshing = true
}

// jika function ini dipanggil maka SwipeRefreshLayout akan disembunyikan
fun SwipeRefreshLayout.hide() {
    isRefreshing = false
}

// jika function ini dipanggil maka views akan ditampilkan
fun View.visible() {
    visibility = View.VISIBLE
}

// jika function ini dipanggil maka views akan dihilangkan
fun View.gone() {
    visibility = View.GONE
}