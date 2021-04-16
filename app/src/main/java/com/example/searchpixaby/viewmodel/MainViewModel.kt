package com.psw.adsloader.githubsearcher.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.searchpixaby.api.IORoutine
import com.example.searchpixaby.api.api
import com.example.searchpixaby.model.Hits

class MainViewModel :ViewModel(){
    var bLoading : MutableLiveData<Boolean> =  MutableLiveData()
    var lst      : MutableLiveData< MutableList<Hits> > = MutableLiveData()
    var message   : MutableLiveData< String > = MutableLiveData()

    var keyword   : String = " "

    var imageDataList = mutableListOf<Hits>()

    init{
        bLoading.value = false
    }

    fun loadImage (page : Int){
        loadImage(keyword, page)
    }

    fun initList (){
        imageDataList.clear()
    }

    fun loadImage(sKeyWord : String,  page : Int = 1) {
        bLoading.postValue(true)

        keyword = sKeyWord

        // 코투틴과 Retrofit 사용방법을 위한 예제
        // UI처리는 반드시 LiveData로 보낸다.
        // 그렇게 하지않으면 Context간의 차이로 App이 종료됨
        IORoutine({
            val key = "API 키값"
            val q = sKeyWord
            val image_type = "photo"
            val data = api.pix.listWithPage(key, q, image_type, page)
            if(data == null) return@IORoutine

            // UI에 전송
            imageDataList.addAll(data.hits)
            lst.postValue(imageDataList)

        }, {
            bLoading.postValue(false)
            message.postValue("$it")
        })

    }

}