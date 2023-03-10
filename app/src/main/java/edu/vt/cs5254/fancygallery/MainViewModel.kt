package edu.vt.cs5254.fancygallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.vt.cs5254.fancygallery.api.GalleryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "GalleryViewModel"

class MainViewModel : ViewModel() {
    private val photoRepository = PhotoRepository()

    private val _galleryItems: MutableStateFlow<List<GalleryItem>> =
        MutableStateFlow(emptyList())
    val galleryItems: StateFlow<List<GalleryItem>>
        get() = _galleryItems.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val items = photoRepository.fetchPhotos(99)
                Log.d(TAG, "Items received: $items")
                _galleryItems.value = items
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch gallery items", ex)
            }
        }
    }

    fun reloadGalleryItems() {
        //empty the gallery item list and restart the fetch process. Remember to fetch exactly 99 items.
        _galleryItems.value = emptyList()
        viewModelScope.launch {
            try {
                val items = photoRepository.fetchPhotos(99)
                Log.d(TAG, "(in reloadGalleryItems()) Items received: $items")
                _galleryItems.value = items
            } catch (ex: Exception) {
                Log.d(TAG, "Failed to fetch gallery items", ex)
            }
        }


    }
}