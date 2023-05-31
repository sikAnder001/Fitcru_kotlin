package com.fitness.fitnessCru.vm_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.viewmodel.SNCProgramViewModel

class SNCProgramVMFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SNCProgramViewModel(repository) as T
    }
}