package edu.vt.cs5254.fancygallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private const val API_KEY = "0ef38a2e45a5fd1eaabba3ff0ef49760"

interface FlickrApi {
    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=$API_KEY" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s,geo"
    )

    suspend fun fetchPhotos(@Query("per_page") perPage: Int): FlickrResponse

}