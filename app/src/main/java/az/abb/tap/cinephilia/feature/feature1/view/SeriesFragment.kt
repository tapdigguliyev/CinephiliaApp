package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.base.BaseAdapter
import az.abb.tap.cinephilia.databinding.FragmentSeriesBinding
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
class SeriesFragment : Fragment() {
    private lateinit var binding: FragmentSeriesBinding
    private val topRatedSeriesAdapter by lazy { BaseAdapter<Media>() }
    private val popularSeriesAdapter by lazy { BaseAdapter<Media>() }
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopRatedTVShowsRecyclerView()
        setupTVShowsRecyclerView()
        setupTopRatedTVShowsAdapter()
        setupTVShowsAdapter()

        observeTopRatedTVShows()
        observePopularTVShows()
    }

    private fun observeTopRatedTVShows() {
        viewModel.topRatedTVShows.observe(viewLifecycleOwner) { responseResource ->
            when (responseResource) {
                is Resource.Success -> {
                    binding.pbTopRatedTVShows.makeInvisible()
                    responseResource.data?.let { response ->
                        topRatedSeriesAdapter.differ.submitList(response.toMedias().movies.toMutableList())
                    }
                }

                is Resource.Error -> {
                    binding.pbTopRatedTVShows.makeInvisible()
                    responseResource.message?.let { message ->
                        Log.e(MoviesFragment.TAG, "An error occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    binding.pbTopRatedTVShows.makeVisible()
                }
            }
        }
    }

    private fun observePopularTVShows() {
        viewModel.popularTVShows.observe(viewLifecycleOwner) { responseResource ->
            when (responseResource) {
                is Resource.Success -> {
                    binding.pbPopularTVShows.makeInvisible()
                    responseResource.data?.let { response ->
                        popularSeriesAdapter.differ.submitList(response.toMedias().movies.toMutableList())
                    }
                }

                is Resource.Error -> {
                    binding.pbPopularTVShows.makeInvisible()
                    responseResource.message?.let { message ->
                        Log.e(MoviesFragment.TAG, "An error occurred: $message")
                        Toast.makeText(requireContext(), "An error occurred: $message", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    binding.pbPopularTVShows.makeVisible()
                }
            }
        }
    }

    private fun setupTopRatedTVShowsAdapter() {
        topRatedSeriesAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemMediaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        topRatedSeriesAdapter.expressionOnBindViewHolder = { topRatedTVShow, viewBinding ->
            val view = viewBinding as ItemMediaBinding

            view.tvMediaName.text = topRatedTVShow.title
            view.tvMediaYear.text = topRatedTVShow.releaseDate.getYearFromDate()
            view.tvMediaGenre.text = topRatedTVShow.getListOfSpecificGenreNames(viewModel.tVShowGenres).toStr()
            view.tvMediaRating.text = topRatedTVShow.rating.outOfTen()
            view.tvMediaLanguage.text = topRatedTVShow.language
            glide.load(topRatedTVShow.imageLink).into(view.ivMedia)

            CoroutineScope(Dispatchers.IO).launch {
                view.mediaItemCard.assignColors(
                    requireContext(),
                    topRatedTVShow.imageLink,
                    glide,
                    view.tvMediaName,
                    view.tvMediaYear,
                    view.tvMediaGenre,
                    view.tvMediaRating,
                    view.tvMediaLanguage
                )
            }

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_seriesFragment_to_movieDetailsFragment, topRatedTVShow.idBundle("SERIES"))
            }
        }
    }

    private fun setupTVShowsAdapter() {
        popularSeriesAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemMediaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        popularSeriesAdapter.expressionOnBindViewHolder = { popularTVShow, viewBinding ->
            val view = viewBinding as ItemMediaBinding

            view.tvMediaName.text = popularTVShow.title
            view.tvMediaYear.text = popularTVShow.releaseDate.getYearFromDate()
            view.tvMediaGenre.text = popularTVShow.getListOfSpecificGenreNames(viewModel.tVShowGenres).toStr()
            view.tvMediaRating.text = popularTVShow.rating.outOfTen()
            view.tvMediaLanguage.text = popularTVShow.language
            glide.load(popularTVShow.imageLink).into(view.ivMedia)

            CoroutineScope(Dispatchers.IO).launch {
                view.mediaItemCard.assignColors(
                    requireContext(),
                    popularTVShow.imageLink,
                    glide,
                    view.tvMediaName,
                    view.tvMediaYear,
                    view.tvMediaGenre,
                    view.tvMediaRating,
                    view.tvMediaLanguage
                )
            }

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_seriesFragment_to_movieDetailsFragment, popularTVShow.idBundle("SERIES"))
            }
        }
    }

    private fun setupTVShowsRecyclerView() {
        binding.rvTVShows.apply {
            adapter = popularSeriesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            snapToChildView()
        }
    }

    private fun setupTopRatedTVShowsRecyclerView() {
        binding.rvTopRatedTVShows.apply {
            adapter = topRatedSeriesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            snapToChildView()
        }
    }
}