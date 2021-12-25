package id.nuryono.adnan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.nuryono.adnan.repository.Repository

// class yang berfungsi menginitiate dan return viewModel
class MainViewModelFactory(private var repository: Repository): ViewModelProvider.Factory {
    // override create method yang akan membiarkan modelClass membuat class viewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}