package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.base.BaseAdapter
import az.abb.tap.cinephilia.databinding.FragmentMoviesBinding
import az.abb.tap.cinephilia.databinding.ItemMediaBinding
import az.abb.tap.cinephilia.feature.feature1.model.Movie
import az.abb.tap.cinephilia.feature.feature1.viewmodel.MainViewModel
import az.abb.tap.cinephilia.utility.Resource
import az.abb.tap.cinephilia.utility.toMoviesResponse
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private lateinit var binding: FragmentMoviesBinding
    private val topRatedMoviesAdapter by lazy { BaseAdapter<Movie>() }
    private val moviesAdapter by lazy { BaseAdapter<String>() }
    private val viewModel: MainViewModel by activityViewModels()

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

        viewModel.topRatedMovies.observe(viewLifecycleOwner) { responseResource ->
            when(responseResource) {
                is Resource.Success -> {
                    hideProgressBar()
                    responseResource.data?.let { response ->
                        topRatedMoviesAdapter.listOfItems = response.toMoviesResponse().movies.toMutableList()
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    responseResource.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        }
    }

    private fun showProgressBar() {
        binding.pbTopRatedMovies.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.pbTopRatedMovies.visibility = View.INVISIBLE
    }

    private fun setupTopRatedMoviesAdapter() {
        topRatedMoviesAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemMediaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        topRatedMoviesAdapter.expressionOnBindViewHolder = { topRatedMovie, viewBinding ->
            val view = viewBinding as ItemMediaBinding
            view.tvMediaName.text = topRatedMovie.title
            view.tvMediaYear.text = topRatedMovie.releaseDate
            view.tvMediaGenre.text = topRatedMovie.genreIds.map { it.toString() }.toString()
            glide.load(topRatedMovie.imageLink).into(view.ivMedia)

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_moviesFragment_to_movieDetailsFragment)
            }
        }
    }

    private fun setupMoviesAdapter() {
        moviesAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemMediaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        moviesAdapter.expressionOnBindViewHolder = { movie, viewBinding ->
            val view = viewBinding as ItemMediaBinding
            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_moviesFragment_to_movieDetailsFragment)
            }
        }
    }

    private fun setupMoviesRecyclerView() {
        binding.rvMovies.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupTopRatedMoviesRecyclerView() {
        binding.rvTopRatedMovies.apply {
            adapter = topRatedMoviesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }
}