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
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.base.BaseAdapter
import az.abb.tap.cinephilia.databinding.FragmentMoviesBinding
import az.abb.tap.cinephilia.databinding.ItemMediaBinding
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
    private val topRatedMoviesAdapter by lazy { BaseAdapter<Media>() }
    private val moviesAdapter by lazy { BaseAdapter<Media>() }
    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var glide: RequestManager

    companion object {
        val TAG = this::class.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopRatedMoviesRecyclerView()
        setupMoviesRecyclerView()
        setupTopRatedMoviesAdapter()
        setupMoviesAdapter()

        observeTopRatedMovies()
        observePopularMovies()
    }

    private fun observeTopRatedMovies() {
        lifecycleScope.launch {
            viewModel.getMovieList().observe(viewLifecycleOwner) {
                it?.let {
                    val mediaPagingData = it.map { result ->
                        result.toMedia()
                    }
                    topRatedMoviesAdapter.submitData(lifecycle, mediaPagingData)
                }
            }
        }
        viewModel.topRatedMovies.observe(viewLifecycleOwner) { responseResource ->
            when (responseResource) {
                is Resource.Success -> {
                    binding.pbTopRatedMovies.makeInvisible()
                    responseResource.data?.let { response ->
//                        topRatedMoviesAdapter.submitData(lifecycle, response.toMedias().movies.toMutableList())
//                        topRatedMoviesAdapter.differ.submitList(response.toMedias().movies.toMutableList())
                    }
                }

                is Resource.Error -> {
                    binding.pbTopRatedMovies.makeInvisible()
                    responseResource.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    binding.pbTopRatedMovies.makeVisible()
                }
            }
        }
    }

    private fun observePopularMovies() {
        viewModel.popularMovies.observe(viewLifecycleOwner) { responseResource ->
            when (responseResource) {
                is Resource.Success -> {
                    binding.pbPopularMovies.makeInvisible()
                    responseResource.data?.let { response ->
//                        moviesAdapter.differ.submitList(response.toMedias().movies.toMutableList())
                    }
                }

                is Resource.Error -> {
                    binding.pbPopularMovies.makeInvisible()
                    responseResource.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                        Toast.makeText(requireContext(), "An error occurred: $message", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    binding.pbPopularMovies.makeVisible()
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
            view.tvMediaGenre.text = topRatedMovie.getListOfSpecificGenreNames(viewModel.movieGenres).toStr()
            view.tvMediaRating.text = topRatedMovie.rating.outOfTen()
            view.tvMediaLanguage.text = topRatedMovie.language
            glide.load(topRatedMovie.imageLink).into(view.ivMedia)

            CoroutineScope(Dispatchers.IO).launch {
                view.mediaItemCard.assignColors(
                    requireContext(),
                    topRatedMovie.imageLink,
                    glide,
                    view.tvMediaName,
                    view.tvMediaYear,
                    view.tvMediaGenre,
                    view.tvMediaRating,
                    view.tvMediaLanguage
                )
            }

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_moviesFragment_to_movieDetailsFragment, topRatedMovie.idBundle("MOVIES"))
            }
        }

        topRatedMoviesAdapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading)
                binding.pbTopRatedMovies.makeVisible()
            else {
                binding.pbTopRatedMovies.makeInvisible()
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(requireContext(), it.error.toString(), Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun setupMoviesAdapter() {
        moviesAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemMediaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        moviesAdapter.expressionOnBindViewHolder = { popularMovie, viewBinding ->
            val view = viewBinding as ItemMediaBinding

            view.tvMediaName.text = popularMovie.title
            view.tvMediaYear.text = popularMovie.releaseDate.getYearFromDate()
            view.tvMediaGenre.text = popularMovie.getListOfSpecificGenreNames(viewModel.movieGenres).toStr()
            view.tvMediaRating.text = popularMovie.rating.outOfTen()
            view.tvMediaLanguage.text = popularMovie.language
            glide.load(popularMovie.imageLink).into(view.ivMedia)

            CoroutineScope(Dispatchers.IO).launch {
                view.mediaItemCard.assignColors(
                    requireContext(),
                    popularMovie.imageLink,
                    glide,
                    view.tvMediaName,
                    view.tvMediaYear,
                    view.tvMediaGenre,
                    view.tvMediaRating,
                    view.tvMediaLanguage
                )
            }

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_moviesFragment_to_movieDetailsFragment, popularMovie.idBundle("MOVIES"))
            }
        }
    }

    private fun setupMoviesRecyclerView() {
        binding.rvMovies.apply {
            adapter = moviesAdapter
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