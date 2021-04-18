# SearchPixaby
Pixabay 검색
> 무료 이미지 제공서비스인 Pixabay의 Open API를 이용한 검색 및 링크
**Open API를 사용하기 위해서는 가입하여 Key**를 얻어야 한다. 무료이며 클릭 몇 번으로 가능한 단순한 작업이다. 

[구글플레이](https://play.google.com/store/apps/details?id=com.psw.searchpixaby)

![](intro2.gif)

- retrofit
- coroutine
- pixabay API(무료이미지 사이트)



1. [pixabay API site ](https://pixabay.com/ko/service/about/api/)에 로그인
2. key값을 가져옴.
3. 다음 소스에 입력

~~~kotlin

    fun loadImage(sKeyWord : String,  page : Int = 1) {
        bLoading.postValue(true)

        keyword = sKeyWord

        // 코투틴과 Retrofit 사용방법을 위한 예제
        // UI처리는 반드시 LiveData로 보낸다.
        // 그렇게 하지않으면 Context간의 차이로 App이 종료됨
        IORoutine({
            val key = "pixabay 계정키값"
            val q = sKeyWord
            val image_type = "photo"
            val data = api.function.listWithPage(key, q, image_type, page)
            if(data == null) return@IORoutine

            // UI에 전송
            imageDataList.addAll(data.hits)
            lst.postValue(imageDataList)

        }, {
            bLoading.postValue(false)
            message.postValue("$it")
        })

    }
~~~

