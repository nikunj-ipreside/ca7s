package com.music.ca7s.api


import com.music.ca7s.utils.AppConstants.*
import com.google.gson.JsonElement
import io.reactivex.Observable
import retrofit2.http.*
import okhttp3.MultipartBody
import retrofit2.http.POST
import retrofit2.http.Multipart




interface ExploreService {

    @GET(HOME_DATA_API)
    fun getHomeDataService(): Observable<JsonElement>

    @GET(DASHBOARD_DATA_API)
    fun getDashboardDataService(): Observable<JsonElement>

    @GET(REDEEM_DATA_API)
    fun getRedeemDataService(): Observable<JsonElement>

    @GET(COUNTRY_DATA_API)
    fun getCountryListService(): Observable<JsonElement>

    @GET(MY_NETWORK_DATA_API)
    fun getMyNetwork(@Query("page") page:Int): Observable<JsonElement>

    @GET(MY_NETWORK_TEAM_DATA_API)
    fun getMyTeamNetwork(@Query("id") id:Int,@Query("page") page:Int): Observable<JsonElement>

    @GET(HOT_LIST_DATA_API)
    fun getHotList(): Observable<JsonElement>

    @GET(SCRATCH_WIN_DATA_API)
    fun getScratchWin(): Observable<JsonElement>

    @GET(TREEVIEW_DATA_API)
    fun getTreeView(): Observable<JsonElement>

    @GET(MYTRANSACTION_DATA_API)
    fun getMyTransaction(@Query("page") page:Int, @Query("filter") filterby:String): Observable<JsonElement>

    @GET(TODAY_TASK_DATA_API)
    fun getTodayTask(): Observable<JsonElement>

    @GET(CPALEAD_TASK_DATA_API)
    fun getCplaLeadTask(@Query("id") id:Int, @Query("country") country:String,
                        @Query("device") device:String,@Query("subid") subid:String
                        ,@Query("subid2") subid2:Int,@Query("subid3") subid3:Any): Observable<JsonElement>
//    ,
//    @Query("show") count:Int,@Query("gaid") gaid:String

    @GET(SHOPPING_DATA_API)
    fun getShoppingData(): Observable<JsonElement>

    @GET(APP_OFF_THE_DAY)
    fun getAppOfDyaData(): Observable<JsonElement>

    @GET(NEWS_TASK_DATA_API)
    fun getNewsTask(@Query("page") page:Int): Observable<JsonElement>

    @GET(AD_JUNCTION_DATA_API)
    fun getAdJunctionTask(@Query("page") page:Int): Observable<JsonElement>

    @GET(AD_JUNCTION_POINT_DATA_API)
    fun getAdJunctionPoint(): Observable<JsonElement>

    @GET(EARN_MORE_DATA_API)
    fun getEarnMoreTask(@Query("page") page:Int): Observable<JsonElement>

    @GET(VIDEO_WALL_TASK_DATA_API)
    fun getVideoTask(): Observable<JsonElement>

    @GET(GET_BANK_DETAIL_DATA_API)
    fun getBankDetail(): Observable<JsonElement>

    @GET(GET_KYC_DETAIL_DATA_API)
    fun getKycDetail(): Observable<JsonElement>

    @GET(GET_ADS_DATA_API)
    fun getAds(): Observable<JsonElement>

    @FormUrlEncoded
    @POST(LOGIN_DATA_API)
    fun getLoginService(@Field("user_id")  user_id:String,@Field("password")  password:String,@Field("device_token")  device_token:String,@Field("device_type")  device_type:String,@Field("otp")  otp:String): Observable<JsonElement>


    @FormUrlEncoded
    @POST(FORGOT_DATA_API)
    fun getForgotServiceService(@Field("id")  id:String,@Field("type")  type:String): Observable<JsonElement>

    @FormUrlEncoded
    @POST(CHANGEPASSWORD_DATA_API)
    fun getChangeService(@Field("password")  password:String,@Field("otp")  otp:String,@Field("user_id")  user_id:String): Observable<JsonElement>


    @FormUrlEncoded
    @POST(CHECKRID_DATA_API)
    fun getCheckRidService(@Field("a_id")  a_id:String): Observable<JsonElement>

//    @FormUrlEncoded
    @POST(UPDATE_ADD_JUNCTION)
    fun getUpDateAdJunction(): Observable<JsonElement>

    @FormUrlEncoded
    @POST(UPDATE_SCRATCH_DATA_API)
    fun getUpdateScratch(@Field("id")  id:Int): Observable<JsonElement>

    @FormUrlEncoded
    @POST(COMPLETE_INACTVIE_TASK_DATA_API)
    fun getCompleteTaskApiService(@Field("id")  id:Int): Observable<JsonElement>

    @FormUrlEncoded
    @POST(COMPLETE_CHALLENGE_TASK_DATA_API)
    fun getCompleteChallengeApiService(@Field("id")  id:Int): Observable<JsonElement>

    @FormUrlEncoded
    @POST(COMPLETE_APP_TASK_DATA_API)
    fun getCompleteAppApiService(@Field("id")  id:Int): Observable<JsonElement>

    @FormUrlEncoded
    @POST(UPDATE_VIDEO_WALL_TASK_DATA_API)
    fun getVideoTaskApiService(@Field("id")  id:Int): Observable<JsonElement>


    @FormUrlEncoded
    @POST(UPDATE_EARN_MORE_TASK_DATA_API)
    fun getEarnMoreTaskApiService(@Field("id")  id:Int): Observable<JsonElement>


    @FormUrlEncoded
    @POST(UPDATE_ADD_JUNCTION_TASK_DATA_API)
    fun getAddJunctionTaskApiService(@Field("id")  id:Int): Observable<JsonElement>

    @FormUrlEncoded
    @POST(COMPLETE_INACTVIE_WEB_TASK_DATA_API)
    fun getCompleteTaskWebApiService(@Field("id")  id:Int): Observable<JsonElement>


    @FormUrlEncoded
    @POST(ADD_BANK_DETAIL_DATA_API)
    fun getAddBankDetailApiService(@Field("bank_name")  bank_name:String,@Field("ac_holder_name")  ac_holder_name:String,
                                   @Field("ac_no")  ac_no:String,@Field("ifsc")  ifsc:String,
                                   @Field("pancard")  pancard:String,@Field("city")  city:String,
                                   @Field("state")  state:String,@Field("address")  address:String,
                                   @Field("pincode")  pincode:String): Observable<JsonElement>

    @FormUrlEncoded
    @POST(ADD_REDEEM_DETAIL_DATA_API)
    fun getAddRedeemDetailApiService(@Field("remark")  remark:String,@Field("req_balance")  req_balance:String
                                 ): Observable<JsonElement>

    @FormUrlEncoded
    @POST(SUPPORT_DETAIL_DATA_API)
    fun getSupportApiService(@Field("support_type")  support_type:String,@Field("email")  email:String,
                                   @Field("name")  name:String,@Field("message")  message:String): Observable<JsonElement>

    @FormUrlEncoded
    @POST(COMPLETED_TODAY_TASK_DATA_API)
    fun getCompleteTodayTaskApiService(@Field("id")  id:Int): Observable<JsonElement>

    @FormUrlEncoded
    @POST(COMPLETED_CPALEAD_TASK_DATA_API)
    fun getCompleteCpaLeadApiService(@Field("camp_id")  camp_id:Int,@Field("user_id")  user_id:Int): Observable<JsonElement>

    @FormUrlEncoded
    @POST(COPLETED_APP_OFF_DAY)
    fun getCompleteAppofDayService(@Field("user_id")  user_id:Int,@Field("app_id")  app_id:Int): Observable<JsonElement>

    @FormUrlEncoded
    @POST(COMPLETED_TODAY_TASK_DATA_API_SHIFT)
    fun getCompleteTodayTaskApiServiceShift(@Field("id")  id:Int,@Field("shift")  shift_id:Int): Observable<JsonElement>

    @FormUrlEncoded
    @POST(UPDATE_NEWS_TASK_DATA_API)
    fun getCompleteNewsTaskApiService(@Field("id")  id:Int): Observable<JsonElement>

    @Multipart
    @POST(ADD_KYC_DETAIL_DATA_API)
    fun getAddKycApiService(@Part pan_card: MultipartBody.Part,@Part id_proof: MultipartBody.Part,@Part cancel_cheque: MultipartBody.Part): Observable<JsonElement>


    @FormUrlEncoded
    @POST(STATE_DATA_API)
    fun getStateListService(@Field("c_code")  c_code:String): Observable<JsonElement>


    @FormUrlEncoded
    @POST(OTP_DATA_API)
    fun getOtpService(@Field("user_id")  user_id:String,@Field("password")  password:String): Observable<JsonElement>

}