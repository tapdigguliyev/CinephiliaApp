package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.databinding.FragmentMovieDetailsBinding
import az.abb.tap.cinephilia.feature.feature1.viewmodel.MainViewModel
import az.abb.tap.cinephilia.utility.assignColors
import az.abb.tap.cinephilia.utility.getListOfSpecificGenreNames
import az.abb.tap.cinephilia.utility.getYearFromDate
import az.abb.tap.cinephilia.utility.toStr
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailsBinding
    private val viewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
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

        viewModel.movie?.let { movie ->
            glide.load(movie.imageLink).into(binding.ivMovie)
            binding.tvMovieName.text = movie.title
            binding.tvMovieOriginalTitle.text = movie.originalTitle
            binding.tvMovieGenres.text = movie.getListOfSpecificGenreNames(viewModel.movieGenres).toStr()
            binding.tvMovieYear.text = movie.releaseDate.getYearFromDate()
            binding.tvMovieDescription.text = movie.overview

            CoroutineScope(Dispatchers.IO).launch {
                binding.movieDetailsLayout.assignColors(
                    requireContext(),
                    movie.imageLink,
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