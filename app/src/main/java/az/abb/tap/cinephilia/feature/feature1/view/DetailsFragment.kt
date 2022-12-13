package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.databinding.FragmentDetailsBinding
import az.abb.tap.cinephilia.feature.feature1.viewmodel.MainViewModel
import az.abb.tap.cinephilia.utility.*
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            val toolbar = this.findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.title = viewModel.movie?.title

            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        Log.d("MainViewModel", viewModel.movie?.title ?: "Null")
        viewModel.movie?.let { movie ->
            Log.d("DetailsFragment", movie.title)
            glide.load(movie.poster_path).into(binding.ivMovie)
            binding.tvMovieName.text = movie.title
            binding.tvMovieOriginalTitle.text = movie.original_title
            binding.tvMovieGenres.text = movie.getGenreNames().toStr()
            binding.tvMovieYear.text = movie.release_date.getYearFromDate()
            binding.tvMovieDescription.text = movie.overview

            CoroutineScope(Dispatchers.IO).launch {
                binding.movieDetailsLayout.assignColors(
                    requireContext(),
                    movie.poster_path,
                    glide,
                    binding.tvMovieName,
                    binding.tvMovieOriginalTitle,
                    binding.tvMovieGenres,
                    binding.tvMovieYear,
                    binding.tvMovieDescription
                )
            }
        }

    }
}