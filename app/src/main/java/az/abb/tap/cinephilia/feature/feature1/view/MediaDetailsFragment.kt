package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.databinding.FragmentMediaDetailsBinding
import az.abb.tap.cinephilia.feature.feature1.model.mediadetails.MediaDetails
import az.abb.tap.cinephilia.feature.feature1.viewmodel.DetailsViewModel
import az.abb.tap.cinephilia.utility.*
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MediaDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMediaDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()
    private val args: MediaDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMediaDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(args.mediaType) {
            "MOVIES" -> {
                viewModel.getMovieDetails(args.mediaId)
                viewModel.movie.observe(viewLifecycleOwner) { responseResource ->
                    when (responseResource) {
                        is Resource.Success -> {
                            binding.pbMovieDetails.makeInvisible()
                            responseResource.data?.let { movieDetailsResponse ->

                                val movie = movieDetailsResponse.toMediaDetails()
                                setToolbar(movie)
                                setViews(movie)
                                CoroutineScope(Dispatchers.IO).launch { setColors(movie) }
                            }
                        }
                        is Resource.Error -> {
                            binding.pbMovieDetails.makeInvisible()
                            responseResource.message?.let { message ->
                                Toast.makeText(
                                    requireContext(),
                                    "An error occurred: $message",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        is Resource.Loading -> {
                            binding.pbMovieDetails.makeVisible()
                        }
                    }
                }
            }
            "SERIES" -> {
                viewModel.getSeriesDetails(args.mediaId)
                viewModel.serie.observe(viewLifecycleOwner) { responseResource ->
                    when (responseResource) {
                        is Resource.Success -> {
                            binding.pbMovieDetails.makeInvisible()
                            responseResource.data?.let { serieDetailsResponse ->

                                val serie = serieDetailsResponse.toMediaDetails()
                                setToolbar(serie)
                                setViews(serie)
                                CoroutineScope(Dispatchers.IO).launch { setColors(serie) }
                            }
                        }
                        is Resource.Error -> {
                            binding.pbMovieDetails.makeInvisible()
                            responseResource.message?.let { message ->
                                Toast.makeText(
                                    requireContext(),
                                    "An error occurred: $message",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        is Resource.Loading -> {
                            binding.pbMovieDetails.makeVisible()
                        }
                    }
                }
            }
        }
    }

    private fun setToolbar(media: MediaDetails) {
        (activity as AppCompatActivity).apply {
            val toolbar = this.findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.title = media.title

            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setColors(media: MediaDetails) {
        binding.movieDetailsLayout.assignColors(
            requireContext(),
            media.poster_path,
            glide,
            binding.tvMovieName,
            binding.tvMovieOriginalTitle,
            binding.tvMovieGenres,
            binding.tvMovieYear,
            binding.tvMovieDescription
        )
    }

    private fun setViews(media: MediaDetails) {
        glide.load(media.poster_path).into(binding.ivMovie)
        binding.tvMovieName.text = media.title
        binding.tvMovieOriginalTitle.text = media.original_title
        binding.tvMovieGenres.text = media.getGenreNames().toStr()
        binding.tvMovieYear.text = media.release_date.getYearFromDate()
        binding.tvMovieDescription.text = media.overview
    }
}