package dev.ujjwal.flickrimagegallery.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dev.ujjwal.flickrimagegallery.model.network.FlickrPhoto
import dev.ujjwal.flickrimagegallery.model.network.FlickrPhotoService
import dev.ujjwal.flickrimagegallery.model.network.FlickrPhotoServiceBuilder
import dev.ujjwal.flickrimagegallery.model.network.FlickrResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var listFlickrPhoto: MutableLiveData<List<FlickrPhoto>>
    private var PAGE_NO = 1
    private var PAGE_SIZE = 20

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }

    fun getFlickrPhoto(): MutableLiveData<List<FlickrPhoto>> {
        if (!::listFlickrPhoto.isInitialized) {
            listFlickrPhoto = MutableLiveData()
            fetchFlickrPhoto()
        }

        return listFlickrPhoto
    }

    fun refresh() {
        fetchFlickrPhoto()
    }

    private fun fetchFlickrPhoto() {

        val flickrPhotoService = FlickrPhotoServiceBuilder.buildService(FlickrPhotoService::class.java)

        val response = flickrPhotoService.getPhotos(PAGE_NO, PAGE_SIZE, "6f102c62f41998d151e5a1b48713cf13")

        response.enqueue(object : Callback<FlickrResponse> {
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {
                if (response.isSuccessful) {
                    val flickrResponse = response.body()
                    val flickrPhotos = flickrResponse?.photos
                    listFlickrPhoto.value = flickrPhotos?.photo
                }
            }
        })
    }
}