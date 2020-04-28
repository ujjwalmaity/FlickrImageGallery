package dev.ujjwal.flickrimagegallery.viewmodel

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.ujjwal.flickrimagegallery.model.network.*
import dev.ujjwal.flickrimagegallery.model.storage.Photo
import dev.ujjwal.flickrimagegallery.model.storage.PhotoDao
import dev.ujjwal.flickrimagegallery.model.storage.PhotoRoomDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var listFlickrPhoto: MutableLiveData<List<FlickrPhoto>>
    var error: MutableLiveData<Boolean> = MutableLiveData()
    private var PAGE_NO = 1
    private var PAGE_SIZE = 20

    private val photoDao: PhotoDao
    internal val allPhoto: LiveData<List<Photo>>

    init {
        val photoDB = PhotoRoomDatabase.getDatabase(application)
        photoDao = photoDB!!.photoDao()
        allPhoto = photoDao.allPhotos
    }

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
        error.value = false

        val flickrPhotoService = FlickrPhotoServiceBuilder.buildService(FlickrPhotoService::class.java)

        val response = flickrPhotoService.getPhotos(PAGE_NO, PAGE_SIZE, "6f102c62f41998d151e5a1b48713cf13")

        response.enqueue(object : Callback<FlickrResponse> {
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                error.value = true
            }

            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {
                if (response.isSuccessful) {
                    val flickrResponse = response.body()
                    val flickrPhotos = flickrResponse?.photos
                    listFlickrPhoto.value = flickrPhotos?.photo
                    deleteData()
                    insertData(flickrPhotos)
                }
            }
        })
    }

    fun deleteData() {
        DeleteAsyncTask(photoDao).execute()
    }

    private class DeleteAsyncTask(private val mPhotoDao: PhotoDao) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            mPhotoDao.deleteAll()
            return null
        }
    }

    private fun insertData(flickrPhotos: FlickrPhotos?) {
        for (tempFlickrPhoto in flickrPhotos?.photo!!) {
            val photoId = UUID.randomUUID().toString()
            val tempPhoto = Photo(
                photoId,
                tempFlickrPhoto.title,
                tempFlickrPhoto.url,
                tempFlickrPhoto.width,
                tempFlickrPhoto.height
            )
            InsertAsyncTask(photoDao).execute(tempPhoto)
        }
    }

    private class InsertAsyncTask(private val mPhotoDao: PhotoDao) :
        AsyncTask<Photo, Void, Void>() {
        override fun doInBackground(vararg notes: Photo): Void? {
            mPhotoDao.insert(notes[0])
            return null
        }
    }
}