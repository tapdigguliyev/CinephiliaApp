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
import az.abb.tap.cinephilia.base.BasePagingAdapter
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
    private val popularSeriesAdapter by lazy { BasePagingAdapter<Media>() }
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var glide: RequestManager

    companion object {
        private val TAG = this::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            observeTopRatedTVShows()
            observePopularTVShows()
        }
    }

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
                        Log.e(TAG, "An error occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    binding.pbTopRatedTVShows.makeVisible()
                }
            }
        }
    }

    private fun observePopularTVShows() {
        lifecycleScope.launch {
            viewModel.getPopularTVShowsList().observe(viewLifecycleOwner) {
                it?.let {
                    val mediaPagingData = it.map { resultSeries ->
                        resultSeries.toMedia()
                    }
                    popularSeriesAdapter.submitData(lifecycle, mediaPagingData)
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

        popularSeriesAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading)
                binding.pbPopularTVShows.makeVisible()
            else {
                binding.pbPopularTVShows.makeInvisible()
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