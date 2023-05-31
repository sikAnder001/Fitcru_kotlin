package com.fitness.fitnessCru.repository

import android.util.Log
import com.fitness.fitnessCru.body.*
import com.fitness.fitnessCru.helper.ResponseModel
import com.fitness.fitnessCru.model.CoachSlabResponse
import com.fitness.fitnessCru.model.ExerciseByWorkoutModel
import com.fitness.fitnessCru.model.MealsPojo
import com.fitness.fitnessCru.model.SNCProgramModel
import com.fitness.fitnessCru.network.Network
import com.fitness.fitnessCru.response.*
import com.fitness.fitnessCru.utility.FCM_TOKEN
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class Repository {

    suspend fun signUpWithEmail(email: String, deviceType: Int): Response<SignUpWithEmailResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().signUpWithEmail(Session.getToken()!!, email, deviceType))
        }
    }

    suspend fun login(
        mobile: String,
        deviceToken: String,
        deviceId: String,
        deviceType: Int,
        quickBloxId: String,

        ): Response<MobileLoginResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(
                Network().mobileLogin(
                    mobile,
                    deviceToken,
                    deviceId,
                    deviceType,
                    quickBloxId
                )
            )
        }
    }

    suspend fun register(body: RegisterBody): Response<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().register(body))
        }
    }

    suspend fun whatBringsYou(): Response<WhatBringsYouResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().whatBringsYou(Session.getToken()!!))
        }
    }

    suspend fun getWorkoutVibe(): Response<WorkoutVibeResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getWorkoutVibe(Session.getToken()!!))
        }
    }

    suspend fun getQuestionByUserID(): Response<GetQuestionsByUserIdResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getQuestionByUserID(Session.getToken()!!))
        }
    }

    suspend fun howActiveYou(): Response<HowActiveYouResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().howActiveYou(Session.getToken()!!))
        }
    }

    suspend fun token(): Response<TokenResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().token())
        }
    }

    suspend fun forgotPassword(email: String): Response<ForgotPasswordResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().forgotPassword(email))
        }
    }

    suspend fun doRegister(signInBody: SignInBody): Response<SignInResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().doRegister(signInBody))
        }
    }

    suspend fun getUserDetails(): Response<UserDetailsResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getUserDetails(Session.getToken()!!))
        }
    }

    suspend fun updateUserDetails(data: UpdateUserDetailsBody): Response<UserDetailsResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(
                Network().updateUserDetails(
                    Session.getToken()!!,
                    data.first_name,
                    data.phone_number,
                    data.email,
                    data.gender,
                    data.dob,
                    data.image
                )
            )
        }
    }

    suspend fun uploadImageQuestionnaire(data: MultipartBody): Response<ImageUploadResponse> {
        return withContext(Dispatchers.IO) {
            Network().uploadImageQuestionnaire(Session.getToken()!!, data)
        }
    }

    suspend fun logout(): Response<LogoutResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().logout(Session.getToken()!!))
        }
    }

    suspend fun changePassword(changePasswordBody: ChangePasswordBody): Response<ChangePasswordResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().changePassword(Session.getToken()!!, changePasswordBody))
        }
    }

    suspend fun createPassword(changePasswordBody: ChangePasswordBody): Response<CreatePasswordResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().createPassword(Session.getToken()!!, changePasswordBody))
        }
    }

    suspend fun emailLogin(
        email: String,
        password: String,
        deviceToken: String,
        deviceId: String,
        deviceType: Int
    ): Response<EmailLoginResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().emailLogin(email, password, deviceToken, deviceId, deviceType))
        }
    }

    suspend fun getDietType(): Response<DietTypeResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getDietType(Session.getToken()!!))
        }
    }

    suspend fun getCountry(): Response<CountriesResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getCountries(Session.getToken()!!))
        }
    }

    suspend fun getState(country: Int): Response<StateResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getState(Session.getToken()!!, country))
        }
    }

    suspend fun getCity(state: Int): Response<CityResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getCity(Session.getToken()!!, state))
        }
    }

    suspend fun getFitnessLevel(): Response<FitnessLevelResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getFitnessLevel(Session.getToken()!!))
        }
    }

    suspend fun otpFun(auth: String, otp: String, quickBloxId: String?): Response<OtpResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().otpVerify(auth, otp, quickBloxId))
        }
    }

    suspend fun resendOtpFun(auth: String, number: String): Response<ResendOtpResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().resendOtp(auth, number))
        }
    }

    suspend fun emailCodeFun(auth: String, code: String): Response<EmailCodeResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().emailVerify(auth, code))
        }
    }

    suspend fun updateQuestion(updateQuestionBody: UpdateQuestionBody): Response<UpdateQuestionResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().workoutVibes(Session.getToken()!!, updateQuestionBody))
        }
    }

    suspend fun updateQuestion2(updateQuestionBody: UpdateQuestionBody2): Response</*UpdateQuestionResponse*/UpdateGetQuestionByUserIDResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().workoutVibes2(Session.getToken()!!, updateQuestionBody))
        }
    }

    suspend fun updateGender(dob: String, gender: String): Response<UpdateGenderDobResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().updateDobGender(Session.getToken()!!, dob, gender))
        }
    }

    suspend fun getCongrats(): Response<CongratulationResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getCongrats(Session.getToken()!!))
        }
    }

    suspend fun getDashBoard(date: String): Response<DashBoardResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getDashBoard(Session.getToken()!!, date))
        }
    }

    suspend fun getCardioList(): Response<CardioListResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getCardioList(Session.getToken()!!))
        }
    }

    suspend fun getProgram(): Response<WorkoutProgramResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getProgram(Session.getToken()!!))
        }
    }

    suspend fun getFoods(search: String): Response<FoodListResposne>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getFoods(Session.getToken()!!, search))
        }
    }

    suspend fun addMeal(body: MealBody): Response<AddMealResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().mealAdd(Session.getToken()!!, body))
        }
    }

    suspend fun activityGoals(body: ActivityBody): Response<ActivityResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().activityGoals(Session.getToken()!!, body))
        }
    }

    suspend fun updateMeal(body: MealBody): Response<UpdateMealResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().mealUpdate(Session.getToken()!!, body))
        }
    }

    suspend fun mealById(id: Int, mDate: String, mTime: String): Response<MealByIdResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().mealById(Session.getToken()!!, id, mDate, mTime))
        }
    }

    suspend fun mealType(selectedDate: String): Response<MealTypeResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().mealType(Session.getToken()!!, selectedDate))
        }
    }

    suspend fun getAllReminder(): Response<GetAllReminderResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getAllReminder(Session.getToken()!!))
        }
    }

    suspend fun getReminderById(id: Int): Response<ReminderCommonResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getReminderByID(Session.getToken()!!, id))
        }
    }

    suspend fun deleteReminder(id: Int): Response<DeleteReminderResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().deleteReminder(Session.getToken()!!, id))
        }
    }

    suspend fun coachWithSpecialization(body: GetCoachListBody): Response<GetCoachListResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().coachWithSpecialization(Session.getToken()!!, body))
        }
    }

    suspend fun coachWithoutSpecialization(body: GetCoachListBody2): Response<GetCoachListResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().coachWithoutSpecialization(Session.getToken()!!, body))
        }
    }

    suspend fun addReminder(body: ReminderBody): Response<ReminderCommonResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().addReminder(Session.getToken()!!, body))
        }
    }

    suspend fun updateReminder(body: ReminderBody): Response<ReminderCommonResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().updateReminder(Session.getToken()!!, body))
        }
    }

    suspend fun getNutritionDashboard(date: String): Response<MealsPojo>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getNutritionDashboard(Session.getToken()!!, date))
        }
    }

    suspend fun getStudio(): Response<StudioResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().studio(Session.getToken()!!))
        }
    }

    suspend fun getRecipeDetails(id: Int): Response<RecipeDetailResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().recipeDetails(Session.getToken()!!, id))
        }
    }

    suspend fun getTrendingOffers(id: Int): Response<TrendingOfferResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getTrendingOffers(Session.getToken()!!, id))
        }
    }

    suspend fun getSncProgram(id: String): Response<SNCProgramModel>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getSNCPrograms(Session.getToken()!!, id))
        }
    }

    suspend fun healthQuestion(body: HealthQuestionBody): Response<HealthQuestionResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().healthQuestion(Session.getToken()!!, body))
        }
    }

    suspend fun addAddress(body: AddressBody): Response<AddressResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(
                if (body.address_id > 0)
                    Network().updateAddress(Session.getToken()!!, body)
                else {
                    Log.v("hello", "add address")
                    Network().addAddress(Session.getToken()!!, body)
                }
            )
        }
    }

    suspend fun getAddressByUser(): Response<GetAddressResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getAddressByUser(Session.getToken()!!))
        }
    }

    suspend fun deleteAddress(id: Int): Response<DeleteReminderResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().deleteAddress(Session.getToken()!!, id))
        }
    }

    suspend fun getExercise(id: Int): Response<ExerciseByWorkoutModel> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getExercise(Session.getToken()!!, id))
        }
    }

    suspend fun getSummary(): Response<MySummaryResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getSummary(Session.getToken()!!))
        }
    }

    suspend fun getSubscriptionPlan(id: Int, slab_id: Int): Response<SubscriptionPlanResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getSubscriptionPlan(Session.getToken()!!, id, slab_id))
        }
    }

    suspend fun getCoachCategory(): Response<CoachCategoryResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getCoachCategory(Session.getToken()!!))
        }
    }

    suspend fun getCoachSlab(): Response<CoachSlabResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getCoachSlab(Session.getToken()!!))
        }
    }

    suspend fun getCoachSpecializations(): Response<GetCoachSpecializationsResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(
                Network().getCoachSpecializations(Session.getToken()!!)
            )
        }
    }

    suspend fun userSelectPlan(): Response<UserSelectPlanResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(
                Network().userSelectPlan(Session.getToken()!!)
            )
        }
    }

    suspend fun getHealthQuestionnaire(): Response<GetHealthQuestionnaire>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getHealthQuestionnaire(Session.getToken()!!))
        }
    }

    suspend fun updateHealthQuestion(body: HealthQuestionBody): Response<HealthQuestionResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().updateHealthQuestion(Session.getToken()!!, body))
        }
    }

    suspend fun getCoachDetails(coach_id: Int): Response<GetCoachDetailsResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getCoachDetails(Session.getToken()!!, coach_id))
        }
    }

    suspend fun getPlanDetail(coach_id: Int, fee_id: Int): Response<GetPlanDetailResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getPlanDetail(Session.getToken()!!, coach_id, fee_id))
        }
    }

    suspend fun getSubscriptionPlanMentalHealth(catId: Int): Response<MentalHealthSubscriptionPlanResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getSubscriptionPlanMentalHealth(Session.getToken()!!, catId))
        }
    }

    suspend fun getHabitsList(): Response<HabitsResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getHabitsList(Session.getToken()!!))
        }
    }

    suspend fun getChooseMonthlyPrice(
        feeId: Int,
        price: String,
        catId: Int,
        slabType: Int, feeStructureKey: String, coachId: Int
    ): Response<ChooseMonthlyPlanResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(
                Network().getChooseMonthlyPrice(
                    Session.getToken()!!,
                    feeId,
                    price,
                    catId,
                    slabType, feeStructureKey, coachId
                )
            )
        }
    }

    suspend fun makeDefaultAddress(addressId: Int, checked: Int): Response<AddressResponse> {
        return withContext(Dispatchers.IO) {
            Network().defaultAddress(Session.getToken()!!, addressId, checked)
        }
    }

    suspend fun sendSocialLoginRequest(
        providerId: String,
        socialId: String,
        email: String,
        phone: String?,
        deviceToken: String,
        deviceId: String,
        deviceType: Int


    ): Response<SocialLoginResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(
                Network().sendSocialLoginRequest(
                    providerId,
                    socialId,
                    email,
                    phone,
                    deviceToken,
                    deviceId, deviceType


                )
            )
        }
    }

    suspend fun bookAppointment(body: BookAppointmentBody): Response<BookAppointmentResponse> {
        return withContext(Dispatchers.IO) {
            Network().bookAppointment(Session.getToken()!!, body)
        }
    }

    suspend fun availableTimeSlot(
        coach_id: Int,
        date: String
    ): Response<AvailableTimeSlotResponse> {
        return withContext(Dispatchers.IO) {
            Network().availableTimeSlot(Session.getToken()!!, coach_id, date)
        }
    }

    suspend fun consumedMeal(body: ConsumedMealBody): Response<ConsumedMealResponse> {
        return withContext(Dispatchers.IO) {
            Network().consumedMeal(Session.getToken()!!, body)
        }
    }

    suspend fun notConsumedMeal(body: Int): Response<NotConsumedMealResponse> {
        return withContext(Dispatchers.IO) {
            Network().notConsumedMeal(Session.getToken()!!, body)
        }
    }

    suspend fun payment(basic: String, currency: String, amount: Int): Response<PaymentResponse>? {
        return withContext(Dispatchers.IO) {
            Network().payment(basic, currency, amount)
        }
    }

    suspend fun payNowCoach(
        feeId: Int,
        razorpayPaymentID: String,
        amount: Int,
        id: Int
    ): Response<PayNowResponse>? {
        return withContext(Dispatchers.IO) {
            Network().payNowCoach(Session.getToken()!!, feeId, razorpayPaymentID, amount, id)
        }
    }

    suspend fun deleteFood(i: Int): Response<DeleteFoodResponse>? {
        return withContext(Dispatchers.IO) {
            Network().deleteFood(Session.getToken()!!, i)
        }
    }

    suspend fun getMealDetail(
        id: Int,
        date: String,
        time: String
    ): Response<MealTypeDetailResponse>? {
        return withContext(Dispatchers.IO) {
            Network().getMealDetail(Session.getToken()!!, id, date, time)
        }
    }

    suspend fun buyNowProgram(
        programId: Int,
        razorpayPaymentID: String,
        amount: String
    ): Response<PayNowResponse>? {
        return withContext(Dispatchers.IO) {
            Network().buyNowProgram(Session.getToken()!!, programId, razorpayPaymentID, amount)
        }
    }

    suspend fun getMembershipPlan(): Response<MembershipPlanResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getMembershipPlan(Session.getToken()!!))
        }
    }

    suspend fun getStudioDetails(studioId: Int): Response<StudioDetailsResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getStudioDetails(Session.getToken()!!, studioId))
        }
    }

    suspend fun getWorkoutByFitness(levelId: Int): Response<WorkoutByFitnessResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getWorkoutByFitness(Session.getToken()!!, levelId))
        }
    }

    suspend fun getWorkoutDetail(id: Int?): Response<WorkoutDetailResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getWorkoutDetail(Session.getToken()!!, id))
        }
    }

    suspend fun upcomingAppointment(selectedDate: String): Response<UpcomingAppointmentResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().upcomingAppointment(Session.getToken()!!, selectedDate))
        }
    }

    suspend fun cancelAppointment(
        id: Int,
        coach_id: Int,
        time_slot_id: Int
    ): Response<BookAppointmentResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(
                Network().cancelAppointment(
                    Session.getToken()!!,
                    id,
                    coach_id,
                    time_slot_id
                )
            )
        }
    }

    suspend fun workoutFeedback(body: WorkoutFeedbackBody): Response<WorkoutFeedbackResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().workoutFeedback(Session.getToken()!!, body))
        }
    }

    suspend fun getCoachList(): Response<CoachListResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getCoachList(Session.getToken()!!))
        }
    }

    suspend fun deactivateAccount(): Response<ChangePasswordResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().deactivateAccount(Session.getToken()!!))
        }
    }

    suspend fun chatList(): Response<ChatListResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().chatList(Session.getToken()!!))
        }
    }

    suspend fun lastMessage(lastMessage: LastMessage): Response<LastMessageResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().lastMessage(Session.getToken()!!, lastMessage))
        }
    }

    suspend fun consumedAllMeal(foodsList: ConsumedAllMealBody): Response<ConsumedAllMealResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().consumedAllMeal(Session.getToken()!!, foodsList))
        }
    }

    suspend fun removeAllConsume(ids: String): Response<NotAllConsumedMealResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().removeAllConsume(Session.getToken()!!, ids))
        }
    }

    suspend fun getCoachByType(): Response<CoachTypeResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getCoachByType(Session.getToken()!!))
        }
    }

    suspend fun coachListing(catId: Int, planId: Int): Response<CoachListingResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().coachListing(Session.getToken()!!, catId, planId))
        }
    }

    suspend fun coachPlan(coachId: Int): Response<CoachPlansResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().coachPlan(Session.getToken()!!, coachId))
        }
    }


    suspend fun planBreakup(
        coachId: Int,
        planType: String,
        price: Int,
        planId: Int
    ): Response<PlanBreakUpResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(
                Network().planBreakup(
                    Session.getToken()!!,
                    coachId,
                    planType,
                    price,
                    planId
                )
            )
        }
    }

    suspend fun coachFilter(
        catId: Int,
        minValue: String,
        maxValue: String,
        gender: String?,
        grade: String?,
        specialization: String?
    ): Response<CoachListingResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(
                Network().coachFilter(
                    Session.getToken()!!,
                    catId,
                    minValue,
                    maxValue,
                    gender,
                    grade,
                    specialization
                )
            )
        }
    }

    suspend fun callBack(nameT: String, numT: String): Response<CoachListingResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().callBack(Session.getToken()!!, nameT, numT))
        }
    }

    suspend fun getGradeAndSpecial(): Response<GradeAndSpecialResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().getGradeAndSpecial(Session.getToken()!!))
        }
    }

    suspend fun userSelectedPlan(
        planId: Int,
        coachId: Int?,
        price: Int,
        catId: Int?,
        duration: Int,
        orderId: String
    ): Response<GradeAndSpecialResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(
                Network().userSelectedPlan(
                    Session.getToken()!!,
                    planId,
                    coachId,
                    price,
                    catId,
                    duration,
                    orderId
                )
            )
        }
    }

    suspend fun sessionCompleted(
        workoutId: Int,
        sessionType: String
    ): Response<WorkoutFeedbackResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().sessionCompleted(Session.getToken()!!, workoutId, sessionType))
        }
    }

    suspend fun deleteAccount(): Response<LogoutResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().deleteAccount(Session.getToken()!!))
        }
    }

    suspend fun activateEmailAccount(email: String): Response<SignUpWithEmailResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().activateEmailAccount(email))
        }
    }

    suspend fun activateNumberAccount(number: String): Response<MobileLoginResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().activateNumberAccount(number))
        }
    }

    suspend fun notificationList(): Response<NotificationListResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().notificationList(Session.getToken()!!))
        }
    }

    suspend fun readNotification(id: Int): Response<ReadNotificationResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().readNotification(Session.getToken()!!, id))
        }
    }

    suspend fun saveFcmToken(): Response<FcmTokenResponse>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().saveFcmToken(Session.getToken()!!, Hawk.get(FCM_TOKEN)))
        }
    }

    suspend fun downloadPDF(selectedDate: String): Response<ResponseBody>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().downloadPDF(Session.getToken()!!, selectedDate))
        }
    }

    suspend fun watchSync(map: MutableMap<String, RequestBody>): Response<ResponseModel>? {
        return withContext(Dispatchers.IO) {
            Util.print(Network().watchSync(Session.getToken()!!, map))
        }
    }

    suspend fun storequickbloxid(
        quickBloxId: String?,
        user_id: String
    ): Response<StoreQuickbloxResponse> {
        return withContext(Dispatchers.IO) {
            Util.print(Network().storequickbloxid(Session.getToken()!!, quickBloxId, user_id))
        }
    }


}
