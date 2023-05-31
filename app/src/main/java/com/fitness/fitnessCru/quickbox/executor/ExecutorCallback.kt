package com.fitness.fitnessCru.quickbox.executor

interface ExecutorTask<T> {
    @Throws(Exception::class)
    fun onBackground(): T

    fun onForeground(result: T)

    fun onError(exception: Exception)
}