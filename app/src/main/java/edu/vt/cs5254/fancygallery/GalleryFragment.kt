package edu.vt.cs5254.fancygallery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.FileProvider
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.imageLoader
import edu.vt.cs5254.fancygallery.api.FlickrApi
import edu.vt.cs5254.fancygallery.databinding.FragmentGalleryBinding
import kotlinx.coroutines.launch
import java.io.File

//KEY = 0ef38a2e45a5fd1eaabba3ff0ef49760

private const val TAG = "GalleryFragment"

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val vm: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        //Add fragment_gallery to app bar
        requireActivity().addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_gallery, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.reload_menu -> {
                        requireContext().imageLoader.memoryCache?.clear()
                        vm.reloadGalleryItems()
                        true
                    }
                    else -> false
                }
            }
        },
            viewLifecycleOwner
        )


        binding.photoGrid.layoutManager = GridLayoutManager(context, 3)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.galleryItems.collect { items ->
                    binding.photoGrid.adapter = GalleryItemAdapter(items) { photoPageUri ->
                        findNavController().navigate(
                            GalleryFragmentDirections.showPhoto(
                                photoPageUri
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}