package dev.ujjwal.flickrimagegallery.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dev.ujjwal.flickrimagegallery.R
import dev.ujjwal.flickrimagegallery.model.network.FlickrPhoto
import dev.ujjwal.flickrimagegallery.model.storage.Photo
import dev.ujjwal.flickrimagegallery.view.adapter.HomeRecyclerViewAdapter
import dev.ujjwal.flickrimagegallery.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val NUM_COLUMNS = 2
    private lateinit var homeRecyclerViewAdapter: HomeRecyclerViewAdapter
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    private lateinit var viewModel: HomeViewModel
    private var listPhoto: List<Photo> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeRecyclerViewAdapter = HomeRecyclerViewAdapter(arrayListOf())
        staggeredGridLayoutManager = StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayout.VERTICAL)
        recycler_view.apply {
            layoutManager = staggeredGridLayoutManager
            adapter = homeRecyclerViewAdapter
        }

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        observeViewModel()

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun observeViewModel() {
        viewModel.getFlickrPhoto().observe(viewLifecycleOwner, Observer { listFlickrPhoto ->
            listFlickrPhoto?.let {
                homeRecyclerViewAdapter.updatePhoto(it)
                swipeRefreshLayout.isRefreshing = false
            }
        })

        viewModel.allPhoto.observe(viewLifecycleOwner, Observer {
            it?.let {
                listPhoto = it
                if (viewModel.error.value!!) {
                    updateRecyclerView()
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    updateRecyclerView()
                }
            }
        })
    }

    private fun updateRecyclerView() {
        val listFlickrPhoto: ArrayList<FlickrPhoto> = ArrayList()
        for (photo in listPhoto) {
            val flickrPhoto = FlickrPhoto()
            flickrPhoto.title = photo.title
            flickrPhoto.url = photo.url_s
            flickrPhoto.width = photo.width_s
            flickrPhoto.height = photo.height_s

            listFlickrPhoto.add(flickrPhoto)
        }

        homeRecyclerViewAdapter.updatePhoto(listFlickrPhoto)
        swipeRefreshLayout.isRefreshing = false
    }
}
