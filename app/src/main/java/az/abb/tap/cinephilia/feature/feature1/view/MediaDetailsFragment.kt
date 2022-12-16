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
import androidx.recyclerview.widget.LinearLayoutManager
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.base.BaseAdapter
import az.abb.tap.cinephilia.databinding.FragmentMediaDetailsBinding
import az.abb.tap.cinephilia.databinding.ItemMediaPersonBinding
import az.abb.tap.cinephilia.feature.feature1.model.mediacast.MediaCast
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
    private val mediaCreditsAdapter by lazy { BaseAdapter<MediaCast>() }

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMediaDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMediaCreditsRecyclerView()
        setupMediaCreditsAdapter()

        when(args.mediaType) {
            "MOVIES" -> {
                viewModel.getMovieCredits(args.mediaId)
                observeMovieCreditsAndMovieDetails()
            }
            "SERIES" -> {
                viewModel.getTVShowCredits(args.mediaId)
                observeTVShowCreditsAndTVShowDetails()
            }
        }
    }

    private fun observeMovieCreditsAndMovieDetails() {
        viewModel.movieCredits.observe(viewLifecycleOwner) { responseResource ->
            when (responseResource) {
                is Resource.Success -> {
                    binding.pbMovieDetails.makeInvisible()

                    viewModel.getMovieDetails(args.mediaId)
                    observeMovieDetails()

                    responseResource.data?.let { movieCreditsResponse ->
                        val movieCredits = movieCreditsResponse.cast.map { it.toMediaCast() }
                        mediaCreditsAdapter.differ.submitList(movieCredits)
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

    private fun observeTVShowCreditsAndTVShowDetails() {
        viewModel.tvShowCredits.observe(viewLifecycleOwner) { responseResource ->
            when (responseResource) {
                is Resource.Success -> {
                    binding.pbMovieDetails.makeInvisible()

                    viewModel.getSeriesDetails(args.mediaId)
                    observeTVShowDetails()

                    responseResource.data?.let { tvShowCreditsResponse ->
                        val tvShowCredits = tvShowCreditsResponse.cast.map { it.toMediaCast() }
                        mediaCreditsAdapter.differ.submitList(tvShowCredits)
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

    private fun observeTVShowDetails() {
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

    private fun observeMovieDetails() {
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

    private fun setupMediaCreditsAdapter() {
        mediaCreditsAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemMediaPersonBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        mediaCreditsAdapter.expressionOnBindViewHolder = { mediaCast, viewBinding ->
            val view = viewBinding as ItemMediaPersonBinding

            view.tvMediaPersonName.text = mediaCast.name
            view.tvMediaCharacterName.text = mediaCast.characterName ?: "No character name"
            if (mediaCast.profilePath != null) {
                glide.load(mediaCast.profilePath).into(view.ivMediaPerson)
            } else {
                view.ivMediaPerson.setImageResource(R.drawable.ic_baseline_person_24)
            }


            CoroutineScope(Dispatchers.IO).launch {
                mediaCast.profilePath?.let {
                    view.mediaPersonCard.assignColors(
                        requireContext(),
                        it,
                        glide,
                        view.tvMediaPersonName,
                        view.tvMediaCharacterName,
                        view.tvCharacterName
                    )
                }
            }

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_detailsFragment_to_personDetailFragment, mediaCast.idBundle())
            }
        }
    }

    private fun setupMediaCreditsRecyclerView() {
        binding.rvMediaCredits.apply {
            adapter = mediaCreditsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            snapToChildView()
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
        media.poster_path?.let {
            binding.mediaDetailsBase.assignColors(
                requireContext(),
                it,
                glide,
                binding.tvMovieName,
                binding.tvMovieOriginalTitle,
                binding.tvMovieGenres,
                binding.tvMovieYear,
                binding.tvMovieDescription,
                binding.tvCast,
                binding.tvMovieLanguage,
                binding.tvMovieRating
            )
        }
    }

    private fun setViews(media: MediaDetails) {
        if (media.poster_path != null) {
            glide.load(media.poster_path).into(binding.ivMovie)
        }
        else {
            binding.ivMovie.setImageResource(R.drawable.ic_baseline_perm_media_24)
        }

        binding.tvMovieName.text = media.title
        binding.tvMovieOriginalTitle.text = media.original_title
        binding.tvMovieGenres.text = media.getGenreNames().toStr()
        binding.tvMovieYear.text = media.release_date.getYearFromDate()
        binding.tvMovieDescription.text = media.overview ?: ""
        binding.tvMovieLanguage.text = media.language
        binding.tvMovieRating.text = media.vote_average.outOfTen()
    }
}