package com.fitness.fitnessCru.vm_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.viewmodel.GetQuestionsByUserIdViewModel

class GetQuestionsByUserIdVMFactory(private val repository: Repository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetQuestionsByUserIdViewModel(repository) as T
    }

}