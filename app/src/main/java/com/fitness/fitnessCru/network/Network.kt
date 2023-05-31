package com.fitness.fitnessCru.network

import com.fitness.fitnessCru.body.*
import com.fitness.fitnessCru.helper.ResponseModel
import com.fitness.fitnessCru.model.CoachSlabResponse
import com.fitness.fitnessCru.model.ExerciseByWorkoutModel
import com.fitness.fitnessCru.model.MealsPojo
import com.fitness.fitnessCru.model.SNCProgramModel
import com.fitness.fitnessCru.network.API.Companion.STORE_CLIENT_QUICKBLOX_ID
import com.fitness.fitnessCru.response.*
import com.fitness.fitnessCru.utility.Constants
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface Network {

    /*@Field("quick_blox_id") quick_blox_id : String*/

    @FormUrlEncoded
    @POST(API.SIGNUP_WITH_EMAIL)
    suspend fun signUpWithEmail(
        @Header(Constants.AUTHORIZATION) token: String,
        @Field("email") email: String,
        @Field("device_type") deviceType: Int
    ): Response<SignUpWithEmailResponse>

    @GET(API.SNC_PROGRAM)
    suspend fun getSNCPrograms(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("program_id") date: String
    ): Response<SNCProgramModel>

    @FormUrlEncoded
    @POST(API.MOBILE_LOGIN)
    suspend fun mobileLogin(
        @Field("phone_number") mobile: String,
        @Field("device_token") deviceToken: String,
        @Field("device_id") deviceId: String,
        @Field("device_type") deviceType: Int,
        @Field("quickblox") quickblox: String,


        ): Response<MobileLoginResponse>

    @FormUrlEncoded
    @POST(API.EMAIL_LOGIN)
    suspend fun emailLogin(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_token") token: String,
        @Field("device_id") dId: String,
        @Field("device_type") dt: Int
    ): Response<EmailLoginResponse>

    @Headers()
    @POST(API.SIGNUP)
    suspend fun register(@Body body: RegisterBody): Response<RegisterResponse>


    @POST(API.DELETE_FOOD)
    suspend fun deleteFood(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("id") id: Int
    ): Response<DeleteFoodResponse>

    @FormUrlEncoded
    @POST(API.OTP_VERIFY)
    suspend fun otpVerify(
        @Header(Constants.AUTHORIZATION) token: String,
        @Field("otp") otp: String,
        @Field("quickBloxId") quickBloxId: String?
    ): Response<OtpResponse>

    @FormUrlEncoded
    @POST(API.RESEND_OTP)
    suspend fun resendOtp(
        @Header(Constants.AUTHORIZATION) token: String,
        @Field("phone_number") number: String
    ): Response<ResendOtpResponse>

    @FormUrlEncoded
    @POST(API.EMAIL_VERIFY)
    suspend fun emailVerify(
        @Header(Constants.AUTHORIZATION) token: String,
        @Field("email_verification_code") code: String
    ): Response<EmailCodeResponse>

    @POST(API.SIGNUP)
    suspend fun doRegister(@Body signInBody: SignInBody): Response<SignInResponse>

    @GET(API.WHAT_BRINGS_YOU)
    suspend fun whatBringsYou(@Header(Constants.AUTHORIZATION) token: String): Response<WhatBringsYouResponse>

    @GET(API.WORKOUT_VIBE)
    suspend fun getWorkoutVibe(@Header(Constants.AUTHORIZATION) token: String): Response<WorkoutVibeResponse>

    @GET(API.HOW_ACTIVE_YOU)
    suspend fun howActiveYou(@Header(Constants.AUTHORIZATION) token: String): Response<HowActiveYouResponse>

    @GET(API.STUDIO)
    suspend fun studio(@Header(Constants.AUTHORIZATION) token: String): Response<StudioResponse>

    @GET(API.RECIPE_DETAILS)
    suspend fun recipeDetails(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("food_id") id: Int
    ): Response<RecipeDetailResponse>

    @GET(API.TRENDING_OFFERS)
    suspend fun getTrendingOffers(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("program_id") id: Int
    ): Response<TrendingOfferResponse>

    @GET(API.COUNTRY)
    suspend fun getCountries(
        @Header(Constants.AUTHORIZATION) token: String,
    ): Response<CountriesResponse>

    @GET(API.STATE)
    suspend fun getState(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("country_id") id: Int
    ): Response<StateResponse>

    @GET(API.CITY)
    suspend fun getCity(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("state_id") id: Int
    ): Response<CityResponse>

    @POST(API.CHANGE_PASSWORD)
    suspend fun changePassword(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body changePasswordBody: ChangePasswordBody
    ): Response<ChangePasswordResponse>

    @POST(API.CREATE_PASS)
    suspend fun createPassword(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body changePasswordBody: ChangePasswordBody
    ): Response<CreatePasswordResponse>

    @GET(API.REFRESH_TOKEN)
    suspend fun token(): Response<TokenResponse>

    @FormUrlEncoded
    @POST(API.FORGOT_PASSWORD)
    suspend fun forgotPassword(@Field("email") email: String): Response<ForgotPasswordResponse>

    @POST(API.USER_DETAILS)
    suspend fun getUserDetails(@Header(Constants.AUTHORIZATION) token: String): Response<UserDetailsResponse>

    @Multipart
    @POST(API.UPDATE_PROFILE)
    suspend fun updateUserDetails(
        @Header(Constants.AUTHORIZATION) token: String,
        @Part("first_name") first_name: RequestBody,
        @Part("phone_number") phone_number: RequestBody,
        @Part("email") email: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Response<UserDetailsResponse>


    @POST(API.IMAGE_HEALTH_QUESTIONNAIRE)
    suspend fun uploadImageQuestionnaire(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body image: MultipartBody
    ): Response<ImageUploadResponse>

    @POST(API.LOGOUT)
    suspend fun logout(@Header(Constants.AUTHORIZATION) token: String): Response<LogoutResponse>

    @GET(API.DIET_TYPE)
    suspend fun getDietType(@Header(Constants.AUTHORIZATION) token: String): Response<DietTypeResponse>

    @GET(API.FITNESS_LEVEL)
    suspend fun getFitnessLevel(@Header(Constants.AUTHORIZATION) token: String): Response<FitnessLevelResponse>

    @GET(API.CONGRATULATIONS)
    suspend fun getCongrats(@Header(Constants.AUTHORIZATION) token: String): Response<CongratulationResponse>

    @POST(API.UPDATE_QUESTION)
    suspend fun workoutVibes(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body updateQuestionBody: UpdateQuestionBody
    ): Response<UpdateQuestionResponse>

    @POST(API.UPDATE_QUESTION)
    suspend fun workoutVibes2(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body updateQuestionBody: UpdateQuestionBody2
    ): Response</*UpdateQuestionResponse*/UpdateGetQuestionByUserIDResponse>

    @FormUrlEncoded
    @POST(API.GENDER_DOB)
    suspend fun updateDobGender(
        @Header(Constants.AUTHORIZATION) token: String,
        @Field("dob") dob: String,
        @Field("gender") gender: String
    ): Response<UpdateGenderDobResponse>

    @GET(API.DASHBOARD)
    suspend fun getDashBoard(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("start_date") date: String
    ): Response<DashBoardResponse>

    @GET(API.FOODS)
    suspend fun getFoods(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("search") search: String
    ): Response<FoodListResposne>

    @GET(API.DELETE_ADDRESS)
    suspend fun deleteAddress(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("address_id") id: Int
    ): Response<DeleteReminderResponse>

    @POST(API.MEAL_UPDATE)
    suspend fun mealUpdate(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: MealBody
    ): Response<UpdateMealResponse>

    @POST(API.ADD_ADDRESS)
    suspend fun addAddress(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: AddressBody
    ): Response<AddressResponse>

    @POST(API.UPDATE_ADDRESS)
    suspend fun updateAddress(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: AddressBody
    ): Response<AddressResponse>

    @POST(API.DEFAULT_ADDRESS_BY_USER)
    suspend fun defaultAddress(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("address_id") addressId: Int,
        @Query("is_default") default: Int
    ): Response<AddressResponse>

    @POST(API.MEAL_ADD)
    suspend fun mealAdd(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: MealBody
    ): Response<AddMealResponse>

    @POST(API.ACTIVITY_GOALS)
    suspend fun activityGoals(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: ActivityBody
    ): Response<ActivityResponse>

    @POST(API.HEALTH_QUESTIONNAIRE)
    suspend fun healthQuestion(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: HealthQuestionBody
    ): Response<HealthQuestionResponse>

    @GET(API.MEAL_TYPE)
    suspend fun mealType(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("date") selectedDate: String
    ): Response<MealTypeResponse>

    @GET(API.MEAL_BY_ID)
    suspend fun mealById(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("meal_type_id") id: Int,
        @Query("start_date") mDate: String,
        @Query("start_time") mTime: String
    ): Response<MealByIdResponse>


    @GET(API.GET_USER_MEAL)
    suspend fun getMealDetail(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("meal_type_id") id: Int,
        @Query("start_date") date: String,
        @Query("start_time") time: String
    ): Response<MealTypeDetailResponse>

    @GET(API.GET_REMINDER)
    suspend fun getAllReminder(
        @Header(Constants.AUTHORIZATION) token: String
    ): Response<GetAllReminderResponse>

    @GET(API.GET_REMINDER_BY_ID)
    suspend fun getReminderByID(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("reminder_id") id: Int
    ): Response<ReminderCommonResponse>

    @GET(API.DELETE_REMINDER)
    suspend fun deleteReminder(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("reminder_id") id: Int
    ): Response<DeleteReminderResponse>

    @POST(API.COACH_WITH_SPECIALIZATION)
    suspend fun coachWithSpecialization(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: GetCoachListBody
    ): Response<GetCoachListResponse>

    @POST(API.COACH_WITHOUT_SPECIALIZATION)
    suspend fun coachWithoutSpecialization(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: GetCoachListBody2
    ): Response<GetCoachListResponse>

    @POST(API.ADD_REMINDER)
    suspend fun addReminder(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: ReminderBody
    ): Response<ReminderCommonResponse>

    @POST(API.UPDATE_REMINDER)
    suspend fun updateReminder(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: ReminderBody
    ): Response<ReminderCommonResponse>

    @GET(API.PROGRAM)
    suspend fun getProgram(@Header(Constants.AUTHORIZATION) token: String): Response<WorkoutProgramResponse>

    @GET(API.NUTRITION_DASHBOARD)
    suspend fun getNutritionDashboard(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("date") date: String
    ): Response<MealsPojo>

    @GET(API.COACH_SPECIALIZATION)
    suspend fun getCoachSpecializations(@Header(Constants.AUTHORIZATION) token: String): Response<GetCoachSpecializationsResponse>

    @GET(API.USER_SELECT_PLAN)
    suspend fun userSelectPlan(@Header(Constants.AUTHORIZATION) token: String): Response<UserSelectPlanResponse>

    @POST("https://api.razorpay.com/v1/orders")
    suspend fun payment(
        @Header(Constants.AUTHORIZATION) basic: String,
        @Query("currency") currency: String,
        @Query("amount") amount: Int,
    ): Response<PaymentResponse>

    @GET(API.UPCOMING_APPOINTMENT)
    suspend fun upcomingAppointment(
        @Header(Constants.AUTHORIZATION) basic: String,
        @Query("date") selectedDate: String,
    ): Response<UpcomingAppointmentResponse>

    @POST(API.PAY_NOW)
    suspend fun payNowCoach(
        @Header(Constants.AUTHORIZATION) basic: String,
        @Query("user_fee_select_id") currency: Int,
        @Query("token") token: String,
        @Query("amount") amount: Int,
        @Query("id") id: Int,
    ): Response<PayNowResponse>

    companion object {
        val create = null
        operator fun invoke(): Network {
            return if (create == null) {
                var loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                var okHttpClient = OkHttpClient.Builder()
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .addInterceptor(loggingInterceptor)
                    .build()



                Retrofit.Builder()
                    .baseUrl(API.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Network::class.java)
            } else create!!
        }
    }

    @GET(API.GET_HABITS)
    suspend fun getHabitsList(@Header(Constants.AUTHORIZATION) token: String): Response<HabitsResponse>

    @GET(API.CARDIO_LIST)
    suspend fun getCardioList(@Header(Constants.AUTHORIZATION) token: String): Response<CardioListResponse>

    @GET(API.GET_ADDRESS_BY_USER)
    suspend fun getAddressByUser(@Header(Constants.AUTHORIZATION) token: String): Response<GetAddressResponse>

    @GET(API.EXERCISE)
    suspend fun getExercise(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("workout_id") id: Int
    ): Response<ExerciseByWorkoutModel>

    @GET(API.MY_SUMMARY)
    suspend fun getSummary(
        @Header(Constants.AUTHORIZATION) token: String,
    ): Response<MySummaryResponse>

    @POST(API.SUBSCRIPTION)
    suspend fun getSubscriptionPlan(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("coach_category_id") id: Int,
        @Query("coach_slab_type") slab_id: Int
    ): Response<SubscriptionPlanResponse>

    @GET(API.COACH_CATEGORY)
    suspend fun getCoachCategory(
        @Header(Constants.AUTHORIZATION) token: String,
    ): Response<CoachCategoryResponse>

    @GET(API.COACH_SLAB)
    suspend fun getCoachSlab(
        @Header(Constants.AUTHORIZATION) token: String,
    ): Response<CoachSlabResponse>

    @GET(API.GET_QUESTIONNAIRE)
    suspend fun getHealthQuestionnaire(
        @Header(Constants.AUTHORIZATION) token: String,
    ): Response<GetHealthQuestionnaire>

    @POST(API.UPDATE_QUESTIONNAIRE)
    suspend fun updateHealthQuestion(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: HealthQuestionBody
    ): Response<HealthQuestionResponse>

    @GET(API.GET_COACH_DETAILS)
    suspend fun getCoachDetails(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("coach_id") id: Int
    ): Response<GetCoachDetailsResponse>

    @GET(API.PLAN_DETAIL)
    suspend fun getPlanDetail(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("coach_id") coach_id: Int,
        @Query("user_plan_id") fee_id: Int
    ): Response<GetPlanDetailResponse>

    @GET(API.AVAILABLE_TIME_SLOT)
    suspend fun availableTimeSlot(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("coach_id") coach_id: Int,
        @Query("start_date") date: String
    ): Response<AvailableTimeSlotResponse>

    @POST(API.BOOK_APPOINTMENT)
    suspend fun bookAppointment(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: BookAppointmentBody
    ): Response<BookAppointmentResponse>

    @POST(API.SUBSCRIPTION)
    suspend fun getSubscriptionPlanMentalHealth(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("coach_category_id") catId: Int
    ): Response<MentalHealthSubscriptionPlanResponse>

    @POST(API.CHOOSE_MONTHLY_PRICE)
    suspend fun getChooseMonthlyPrice(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("feestructure_id") fee_id: Int,
        @Query("user_select_price") price: String,
        @Query("coach_category_id") cat_id: Int,
        @Query("coach_slab_type") slab_type: Int,
        @Query("fee_structure_key") feeStructureKey: String,
        @Query("coach_id") coachId: Int,

        ): Response<ChooseMonthlyPlanResponse>


    @POST(API.SOCIAL_MEDIA)
    suspend fun sendSocialLoginRequest(
        @Query("login_provider") providerId: String,
        @Query("social_id") socialId: String,
        @Query("email") email: String,
        @Query("phone_number") mobile: String?,
        @Query("device_token") deviceToken: String,
        @Query("device_id") deviceId: String,
        @Query("device_type") deviceType: Int


    ): Response<SocialLoginResponse>

    @POST(API.USER_CONSUMED_MEAL)
    suspend fun consumedMeal(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: ConsumedMealBody
    ): Response<ConsumedMealResponse>

    @POST(API.USER_NOT_CONSUMED_MEAL)
    suspend fun notConsumedMeal(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("consumed_meal_id") body: Int
    ): Response<NotConsumedMealResponse>

    @POST(API.CONSUMED_ALL)
    suspend fun consumedAllMeal(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body foodsList: ConsumedAllMealBody
    ): Response<ConsumedAllMealResponse>


    @POST(API.REMOVE_ALL_CONSUME)
    suspend fun removeAllConsume(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("consumed_meal_ids") ids: String
    ): Response<NotAllConsumedMealResponse>


    @POST(API.BUY_NOW_PROGRAM)
    suspend fun buyNowProgram(
        @Header(Constants.AUTHORIZATION) basic: String,
        @Query("program_id") currency: Int,
        @Query("stripe_token") token: String,
        @Query("amount") amount: String,
    ): Response<PayNowResponse>

    @GET(API.MEMBERSHIP_PLAN)
    suspend fun getMembershipPlan(@Header(Constants.AUTHORIZATION) token: String): Response<MembershipPlanResponse>

    @GET(API.GET_STUDIO_DETAILS)
    suspend fun getStudioDetails(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("studio_id") studioId: Int
    ): Response<StudioDetailsResponse>

    @GET(API.GET_WORKOUT_BY_FITNESS)
    suspend fun getWorkoutByFitness(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("fitness_level_id") levelId: Int
    ): Response<WorkoutByFitnessResponse>

    @GET(API.GET_QUESTIONS_BY_USER_ID)
    suspend fun getQuestionByUserID(@Header(Constants.AUTHORIZATION) token: String): Response<GetQuestionsByUserIdResponse>

    @GET(API.WORKOUT_DETAIL_BY)
    suspend fun getWorkoutDetail(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("workout_id") id: Int?
    ): Response<WorkoutDetailResponse>

    @POST(API.CANCEL_APPOINTMENT)
    suspend fun cancelAppointment(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("appointment_id") id: Int?,
        @Query("coach_id") caoch_id: Int,
        @Query("coach_time_slot_id") time_slot_id: Int
    ): Response<BookAppointmentResponse>

    @POST(API.WORKOUT_FEEDBACK)
    suspend fun workoutFeedback(
        @Header(Constants.AUTHORIZATION) basic: String, @Body body: WorkoutFeedbackBody
    ): Response<WorkoutFeedbackResponse>

    @GET(API.COACH_LIST)
    suspend fun getCoachList(
        @Header(Constants.AUTHORIZATION) basic: String
    ): Response<CoachListResponse>

    @GET(API.DEACTIVATE_ACCOUNT)
    suspend fun deactivateAccount(
        @Header(Constants.AUTHORIZATION) token: String,
    ): Response<ChangePasswordResponse>

    @GET(API.CHAT_LIST)
    suspend fun chatList(
        @Header(Constants.AUTHORIZATION) token: String,
    ): Response<ChatListResponse>

    @POST(API.LAST_MESSAGE)
    suspend fun lastMessage(
        @Header(Constants.AUTHORIZATION) token: String,
        @Body body: LastMessage
    ): Response<LastMessageResponse>

    @GET(API.COACH_BY_TYPE)
    suspend fun getCoachByType(
        @Header(Constants.AUTHORIZATION) token: String,
    ): Response<CoachTypeResponse>

    @GET(API.COACH_LISTING)
    suspend fun coachListing(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("category_id") cat_id: Int,
        @Query("plan_id") planId: Int
    ): Response<CoachListingResponse>

    @GET(API.COACH_PLANS)
    suspend fun coachPlan(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("coach_id") coachId: Int
    ): Response<CoachPlansResponse>

    @GET(API.COACH_FILTER)
    suspend fun coachFilter(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("category_id") catId: Int,
        @Query("min_price") minValue: String? = null,
        @Query("max_price") maxValue: String? = null,
        @Query("gender") gender: String? = null,
        @Query("grade") grade: String? = null,
        @Query("coach_specialization_id") specialization: String?
    ): Response<CoachListingResponse>

    @POST(API.CALL_BACK)
    suspend fun callBack(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("name") nameT: String,
        @Query("mobile_no") numT: String
    ): Response<CoachListingResponse>


    @GET(API.PLANS_BREAKUP)
    suspend fun planBreakup(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("coach_id") coachId: Int? = null,
        @Query("plan_type") planType: String? = null,
        @Query("price") price: Int? = null,
        @Query("plan_id") planId: Int? = null
    ): Response<PlanBreakUpResponse>

    @GET(API.GRADE_SPECIAL)
    suspend fun getGradeAndSpecial(
        @Header(Constants.AUTHORIZATION) token: String,
    ): Response<GradeAndSpecialResponse>

    @POST(API.USER_SELECT)
    suspend fun userSelectedPlan(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("subscription_plan_id") planId: Int,
        @Query("coach_id") coachId: Int? = null,
        @Query("price") price: Int?,
        @Query("coach_category_ids") catId: Int? = null,
        @Query("duration") duration: Int,
        @Query("order_id") orderId: String
    ): Response<GradeAndSpecialResponse>

    @POST(API.SESSION_COMPLETE)
    suspend fun sessionCompleted(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("session_id") workoutId: Int,
        @Query("session_type") sessionType: String
    ): Response<WorkoutFeedbackResponse>


    /* @POST(API.ACCOUNT_ACTIVATE_WITH_EMAIL)
     suspend fun activateWithEmail(
         @Header(Constants.AUTHORIZATION) token: String,
         @Field("email") email: String
     ): Response<ActivateWithEmailResponse>*/


    /* @POST(API.ACCOUNT_ACTIVATE_WITH_MOBILE)
     suspend fun activateWithMobile(
         @Header(Constants.AUTHORIZATION) token: String,
         @Field("phone_number") phoneNumber: String
     ): Response<>*/

    /*  @GET(API.DELETE_USER)
      suspend fun deleteUser(
          @Header(Constants.AUTHORIZATION) token: String,
      ): Response<>*/


    @GET(API.DELETE_ACCOUNT)
    suspend fun deleteAccount(
        @Header(Constants.AUTHORIZATION) token: String
    ): Response<LogoutResponse>

    @POST(API.ACTIVATE_ACCOUNT_EMAIL)
    suspend fun activateEmailAccount(
        @Query("email") email: String
    ): Response<SignUpWithEmailResponse>

    @POST(API.ACTIVATE_ACCOUNT_NUMBER)
    suspend fun activateNumberAccount(
        @Query("phone_number") number: String
    ): Response<MobileLoginResponse>

    @GET(API.NOTIFICATION_LIST)
    suspend fun notificationList(
        @Header(Constants.AUTHORIZATION) token: String
    ): Response<NotificationListResponse>

    @POST(API.READ_NOTIFICATION)
    suspend fun readNotification(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("notification_id") id: Int
    ): Response<ReadNotificationResponse>

    @POST(API.FCM_TOKEN)
    suspend fun saveFcmToken(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("token") fcmToken: String?
    ): Response<FcmTokenResponse>

    @GET(API.DOWNLOAD_PDF)
    suspend fun downloadPDF(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("date") date: String
    )
            : Response<ResponseBody>

    @Multipart
    @POST(API.WATCH_SYNC)
    suspend fun watchSync(
        @Header(Constants.AUTHORIZATION) token: String,
        @PartMap map: MutableMap<String, RequestBody>
    )
            : Response<ResponseModel>


    @POST(STORE_CLIENT_QUICKBLOX_ID)
    suspend fun storequickbloxid(
        @Header(Constants.AUTHORIZATION) token: String,
        @Query("quickBloxId") quickBloxId: String?,
        @Query("user_id") user_id: String,


        ): Response<StoreQuickbloxResponse>

}