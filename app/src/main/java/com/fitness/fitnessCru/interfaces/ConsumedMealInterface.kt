package com.fitness.fitnessCru.interfaces

import com.fitness.fitnessCru.response.DashBoardResponse

interface ConsumedMealInterface {
    fun click(body: DashBoardResponse.Data.TodayTask)
}