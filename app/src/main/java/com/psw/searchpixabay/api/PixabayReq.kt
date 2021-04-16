package com.psw.searchpixabay.api

import com.psw.searchpixabay.model.RequestPic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.*
import retrofit2.http.GET


interface PixabayReq {

    // 일반적인 API
    @GET("/api")
    suspend fun listWithPage(@Query("key") key: String, @Query("q") q : String, @Query("photo") photo : String, @Query("page") page : Int): RequestPic

}

// >> 코루틴으로 처리하기 <<
// UI처리는 반드시 LiveData로 보낸다.
// 그렇게 하지않으면 Context 변환을 하지 않으므로 앱이 종료된다.
fun IORoutine(fnProcess: suspend CoroutineScope.() -> Unit, fnError : suspend CoroutineScope.(e :Exception)->Unit){
    CoroutineScope(Dispatchers.IO).launch {
        try{
            fnProcess()
        }
        catch (e: Exception){
            fnError(e)
        }
    }
}

