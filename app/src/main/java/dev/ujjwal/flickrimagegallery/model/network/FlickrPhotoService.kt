package dev.ujjwal.flickrimagegallery.model.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrPhotoService {

    @GET("services/rest/?method=flickr.photos.getRecent&format=json&nojsoncallback=1&extras=url_s")
    fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") size: Int,
        @Query("api_key") key: String
    ): Call<FlickrResponse>

}