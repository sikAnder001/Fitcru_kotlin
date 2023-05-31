package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.model.CoachSlabResponse
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.*
import kotlinx.coroutines.launch
import retrofit2.Response

class CoachCategoryViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<CoachCategoryResponse>>()
    val response1 = MutableLiveData<Response<CoachSlabResponse>>()
    val getCoachResponse = MutableLiveData<Response<GetCoachDetailsResponse>>()
    val subPlanResponse = MutableLiveData<Response<SubscriptionPlanResponse>>()
    val getPlanDetailResponse = MutableLiveData<Response<GetPlanDetailResponse>>()
    val subPlanMentalHealthResponse =
        MutableLiveData<Response<MentalHealthSubscriptionPlanResponse>>()

    val payment = MutableLiveData<Response<PaymentResponse>>()
    val payCoach = MutableLiveData<Response<PayNowResponse>>()
    val buyNowProgram = MutableLiveData<Response<PayNowResponse>>()

    val monthlyPlanResponse = MutableLiveData<Response<ChooseMonthlyPlanResponse>>()
    val coachType = MutableLiveData<Response<CoachTypeResponse>>()
    val coachListing = MutableLiveData<Response<CoachListingResponse>>()
    val coachPlan = MutableLiveData<Response<CoachPlansResponse>>()
    val coachFilter = MutableLiveData<Response<CoachListingResponse>>()
    val callBack = MutableLiveData<Response<CoachListingResponse>>()

    val planBreakupResponse = MutableLiveData<Response<PlanBreakUpResponse>>()
    val getGradeAndSpecial = MutableLiveData<Response<GradeAndSpecialResponse>>()
    val userSelectedPlan = MutableLiveData<Response<GradeAndSpecialResponse>>()

    fun getCoachCategory() {
        viewModelScope.launch {
            response.value = repository.getCoachCategory()
        }
    }

    fun getCoachSlab() {
        viewModelScope.launch {
            response1.value = repository.getCoachSlab()
        }
    }

    fun getCoachDetails(coach_id: Int) {
        viewModelScope.launch {
            getCoachResponse.value = repository.getCoachDetails(coach_id)
        }
    }

    fun getSubscriptionPlan(id: Int, slab_id: Int) {
        viewModelScope.launch {
            subPlanResponse.value = repository.getSubscriptionPlan(id, slab_id)
        }
    }

    fun getPlanDetail(coach_id: Int, fee_id: Int) {
        viewModelScope.launch {
            getPlanDetailResponse.value = repository.getPlanDetail(coach_id, fee_id)
        }
    }

    fun getSubscriptionPlanMentalHealth(catId: Int) {
        viewModelScope.launch {
            subPlanMentalHealthResponse.value = repository.getSubscriptionPlanMentalHealth(catId)
        }
    }

    fun getChooseMonthlyPrice(
        feeId: Int,
        feeStructureKey: String,
        price: String,
        categoryId: Int,
        slabId: Int,
        coachId: Int
    ) {
        viewModelScope.launch {
            monthlyPlanResponse.value =
                repository.getChooseMonthlyPrice(
                    feeId,
                    price,
                    categoryId,
                    slabId,
                    feeStructureKey,
                    coachId
                )
        }
    }

    fun payment(razorPayKey: String, currency: String, amount: Int) {
        viewModelScope.launch {
            payment.value = repository.payment(razorPayKey, currency, amount)
        }
    }

    fun buyNowProgram(programId: Int, razorpayPaymentID: String, amount: String) {
        viewModelScope.launch {
            buyNowProgram.value = repository.buyNowProgram(programId, razorpayPaymentID, amount)
        }
    }

    fun getCoachByType() {
        viewModelScope.launch {
            coachType.value = repository.getCoachByType()
        }
    }

    fun coachListing(catId: Int, planId: Int) {
        viewModelScope.launch {
            coachListing.value = repository.coachListing(catId, planId)
        }
    }

    fun coachPlan(coachId: Int) {
        viewModelScope.launch {
            coachPlan.value = repository.coachPlan(coachId)
        }
    }

    fun coachFilter(
        catId: Int,
        minValue: String,
        maxValue: String,
        gender: String?,
        grade: String?,
        specialization: String?
    ) {
        viewModelScope.launch {
            coachFilter.value =
                repository.coachFilter(catId, minValue, maxValue, gender, grade, specialization)
        }
    }

    fun callBack(nameT: String, numT: String) {
        viewModelScope.launch {
            callBack.value = repository.callBack(nameT, numT)
        }
    }

    fun planDetailsBreakup(coachId: Int, planType: String, price: Int, planId: Int) {
        viewModelScope.launch {
            planBreakupResponse.value = repository.planBreakup(coachId, planType, price, planId)
        }
    }

    fun getGradeAndSpecial() {
        viewModelScope.launch {
            getGradeAndSpecial.value = repository.getGradeAndSpecial()
        }
    }

    fun userSelectedPlan(
        planId: Int,
        coachId: Int?,
        price: Int,
        catId: Int?,
        duration: Int,
        orderId: String
    ) {
        viewModelScope.launch {
            userSelectedPlan.value =
                repository.userSelectedPlan(planId, coachId, price, catId, duration, orderId)
        }
    }

}