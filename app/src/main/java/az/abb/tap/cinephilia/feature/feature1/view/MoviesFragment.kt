package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.base.BaseAdapter
import az.abb.tap.cinephilia.base.BasePagingAdapter
import az.abb.tap.cinephilia.databinding.FragmentMoviesBinding
import az.abb.tap.cinephilia.databinding.ItemMediaBinding
import az.abb.tap.cinephilia.feature.feature1.model.genres.Genre
import az.abb.tap.cinephilia.feature.feature1.model.media.Media
import az.abb.tap.cinephilia.feature.feature1.viewmodel.MainViewModel
import az.abb.tap.cinephilia.utility.*
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private lateinit var binding: FragmentMoviesBinding
    private val topRatedMoviesAdapter by lazy { BasePagingAdapter<Media>() }
    private val popularMoviesAdapter by lazy { BasePagingAdapter<Media>() }
    private val viewModel: MainViewModel by viewModels()
    private val genres: MutableList<Genre> = mutableListOf()

    @Inject
    lateinit var glide: RequestManager

    companion object {
        private val TAG = this::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            viewModel.getMovieGenres()
            observeMovieGenresAndObservers()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopRatedMoviesRecyclerView()
        setupPopularMoviesRecyclerView()
        setupTopRatedMoviesAdapter()
        setupMoviesAdapter()
    }

    private fun observeMovieGenresAndObservers() {
        viewModel.movieGenres.observe(viewLifecycleOwner) { responseResource ->
            when (responseResource) {
                is Resource.Success -> {
                    val genreList = responseResource.data?.genres?.map { it.toGenre() }
                    if (genreList != null) genres.addAll(genreList)

                    observeTopRatedMovies()
                    observePopularMovies()
                }

                is Resource.Error -> {
                    binding.pbTopRatedMovies.makeInvisible()
                    binding.pbPopularMovies.makeInvisible()
                    responseResource.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                        Toast.makeText(requireContext(), "An error occurred: $message", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    binding.pbTopRatedMovies.makeVisible()
                    binding.pbPopularMovies.makeVisible()
                }
            }
        }
    }

    private fun observeTopRatedMovies() {
        lifecycleScope.launch {
            viewModel.getTopRatedMovies().observe(viewLifecycleOwner) {
                it?.let {
                    val mediaPagingData = it.map { result ->
                        result.toMedia()
                    }
                    topRatedMoviesAdapter.submitData(lifecycle, mediaPagingData)
                }
            }
        }
    }

    private fun observePopularMovies() {
        lifecycleScope.launch {
            viewModel.getPopularMovieList().observe(viewLifecycleOwner) {
                it?.let {
                    val mediaPagingData = it.map { result ->
                        result.toMedia()
                    }
                    popularMoviesAdapter.submitData(lifecycle, mediaPagingData)
                }
            }
        }
    }

    private fun setupTopRatedMoviesAdapter() {
        topRatedMoviesAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemMediaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        topRatedMoviesAdapter.expressionOnBindViewHolder = { topRatedMovie, viewBinding ->
            val view = viewBinding as ItemMediaBinding

            view.tvMediaName.text = topRatedMovie.title
            view.tvMediaYear.text = topRatedMovie.releaseDate.getYearFromDate()
            view.tvMediaGenre.text = topRatedMovie.getListOfSpecificGenreNames(genres).toStr()
            view.tvMediaRating.text = topRatedMovie.rating.outOfTen()
            view.tvMediaLanguage.text = topRatedMovie.language
            glide.load(topRatedMovie.imageLink).into(view.ivMedia)

            CoroutineScope(Dispatchers.IO).launch {
                topRatedMovie.imageLink?.let {
                    view.mediaItemCard.assignColors(
                        requireContext(),
                        it,
                        glide,
                        view.tvMediaName,
                        view.tvMediaYear,
                        view.tvMediaGenre,
                        view.tvMediaRating,
                        view.tvMediaLanguage
                    )
                }
            }

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_moviesFragment_to_movieDetailsFragment, topRatedMovie.idBundle("MOVIES"))
            }
        }

        topRatedMoviesAdapter.addLoadStateListener { loadState ->
            loadState.setup(requireContext(), binding.pbTopRatedMovies)
        }
    }

    private fun setupMoviesAdapter() {
        popularMoviesAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemMediaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        popularMoviesAdapter.expressionOnBindViewHolder = { popularMovie, viewBinding ->
            val view = viewBinding as ItemMediaBinding

            view.tvMediaName.text = popularMovie.title
            view.tvMediaYear.text = popularMovie.releaseDate.getYearFromDate()
            view.tvMediaGenre.text = popularMovie.getListOfSpecificGenreNames(genres).toStr()
            view.tvMediaRating.text = popularMovie.rating.outOfTen()
            view.tvMediaLanguage.text = popularMovie.language
            glide.load(popularMovie.imageLink).into(view.ivMedia)

            CoroutineScope(Dispatchers.IO).launch {
                popularMovie.imageLink?.let {
                    view.mediaItemCard.assignColors(
                        requireContext(),
                        it,
                        glide,
                        view.tvMediaName,
                        view.tvMediaYear,
                        view.tvMediaGenre,
                        view.tvMediaRating,
                        view.tvMediaLanguage
                    )
                }
            }

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_moviesFragment_to_movieDetailsFragment, popularMovie.idBundle("MOVIES"))
            }
        }

        popularMoviesAdapter.addLoadStateListener { loadState ->
            loadState.setup(requireContext(), binding.pbPopularMovies)
        }
    }

    private fun setupPopularMoviesRecyclerView() {
        binding.rvPopularMovies.apply {
            adapter = popularMoviesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            snapToChildView()
        }
    }

    private fun setupTopRatedMoviesRecyclerView() {
        binding.rvTopRatedMovies.apply {
            adapter = topRatedMoviesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            snapToChildView()
        }
    }
}