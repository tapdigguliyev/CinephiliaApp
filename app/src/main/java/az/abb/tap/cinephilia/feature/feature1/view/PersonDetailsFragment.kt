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
import az.abb.tap.cinephilia.databinding.FragmentPersonDetailBinding
import az.abb.tap.cinephilia.databinding.ItemPersonMediaBinding
import az.abb.tap.cinephilia.feature.feature1.model.persondetails.PersonDetails
import az.abb.tap.cinephilia.feature.feature1.model.personmediacast.PersonMediaCast
import az.abb.tap.cinephilia.feature.feature1.viewmodel.DetailsViewModel
import az.abb.tap.cinephilia.utility.*
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PersonDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPersonDetailBinding
    private val viewModel: DetailsViewModel by viewModels()
    private val args: PersonDetailsFragmentArgs by navArgs()
    private val personMoviesAdapter by lazy { BaseAdapter<PersonMediaCast>() }
    private val personTVShowsAdapter by lazy { BaseAdapter<PersonMediaCast>() }

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPersonDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPersonMoviesRecyclerView()
        setupPersonTVShowsRecyclerView()
        setupPersonMoviesAdapter()
        setupPersonTVShowsAdapter()

        viewModel.getPersonDetails(args.personId)
        viewModel.getPersonMovieCredits(args.personId)
        viewModel.getPersonTVShowCredits(args.personId)
        observePersonDetails()
        observePersonMovies()
        observePersonTVShows()
    }

    private fun observePersonDetails() {
        viewModel.person.observe(viewLifecycleOwner) { responseResource ->
            when (responseResource) {
                is Resource.Success -> {
                    binding.pbPersonDetails.makeInvisible()
                    responseResource.data?.let { personDetailsResponse ->

                        val person = personDetailsResponse.toPersonDetails()
                        setToolbar(person)
                        setViews(person)
                        CoroutineScope(Dispatchers.IO).launch { setColors(person) }
                    }
                }
                is Resource.Error -> {
                    binding.pbPersonDetails.makeInvisible()
                    responseResource.message?.let { message ->
                        Toast.makeText(
                            requireContext(),
                            "An error occurred: $message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    binding.pbPersonDetails.makeVisible()
                }
            }
        }
    }

    private fun observePersonMovies() {
        viewModel.personMovieCredits.observe(viewLifecycleOwner) { responseResource ->
            when (responseResource) {
                is Resource.Success -> {
                    binding.pbPersonDetails.makeInvisible()

                    responseResource.data?.let { personMovieCreditsResponse ->
                        val personMovies = personMovieCreditsResponse.cast.map { it.toPersonMediaCast() }

                        if (personMovies.isNotEmpty()) personMoviesAdapter.differ.submitList(personMovies)
                        else binding.llPersonMovieDetails.makeGone()
                    }
                }
                is Resource.Error -> {
                    binding.pbPersonDetails.makeInvisible()
                    responseResource.message?.let { message ->
                        Toast.makeText(
                            requireContext(),
                            "An error occurred: $message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    binding.pbPersonDetails.makeVisible()
                }
            }
        }
    }

    private fun observePersonTVShows() {
        viewModel.personTVShowCredits.observe(viewLifecycleOwner) { responseResource ->
            when (responseResource) {
                is Resource.Success -> {
                    binding.pbPersonDetails.makeInvisible()

                    responseResource.data?.let { personTVShowCreditsResponse ->
                        val personTVShows = personTVShowCreditsResponse.cast.map { it.toPersonMediaCast() }

                        if (personTVShows.isNotEmpty()) personTVShowsAdapter.differ.submitList(personTVShows)
                        else binding.llPersonTVShowDetails.makeGone()
                    }
                }
                is Resource.Error -> {
                    binding.pbPersonDetails.makeInvisible()
                    responseResource.message?.let { message ->
                        Toast.makeText(
                            requireContext(),
                            "An error occurred: $message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    binding.pbPersonDetails.makeVisible()
                }
            }
        }
    }

    private fun setupPersonMoviesAdapter() {
        personMoviesAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemPersonMediaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        personMoviesAdapter.expressionOnBindViewHolder = { personMediaCast, viewBinding ->
            val view = viewBinding as ItemPersonMediaBinding

            if (personMediaCast.character.isBlank()) view.llPersonCharacterInfo.makeGone()

            view.tvPersonMediaName.text = personMediaCast.mediaName
            view.tvPersonMediaRating.text = personMediaCast.voteAverage.outOfTen()
            view.tvPersonMediaReleaseDate.text = personMediaCast.releaseDate.getYearFromDate()
            view.tvPersonCharacterName.text = personMediaCast.character
            if (personMediaCast.posterPath != null) {
                glide.load(personMediaCast.posterPath).into(view.ivPersonMedia)
            } else {
                view.ivPersonMedia.setImageResource(R.drawable.ic_baseline_perm_media_24)
            }

            CoroutineScope(Dispatchers.IO).launch {
                personMediaCast.posterPath?.let {
                    view.personMediaCard.assignColors(
                        requireContext(),
                        it,
                        glide,
                        view.tvPersonMediaName,
                        view.tvPersonMediaRating,
                        view.tvPersonMediaReleaseDate,
                        view.tvAs,
                        view.tvPersonCharacterName
                    )
                }
            }

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_personDetailFragment_to_detailsFragment, personMediaCast.idBundle("MOVIES"))
            }
        }
    }

    private fun setupPersonTVShowsAdapter() {
        personTVShowsAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemPersonMediaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        personTVShowsAdapter.expressionOnBindViewHolder = { personMediaCast, viewBinding ->
            val view = viewBinding as ItemPersonMediaBinding

            if (personMediaCast.character.isBlank()) view.llPersonCharacterInfo.makeGone()

            view.tvPersonMediaName.text = personMediaCast.mediaName
            view.tvPersonMediaRating.text = personMediaCast.voteAverage.outOfTen()
            view.tvPersonMediaReleaseDate.text = personMediaCast.releaseDate.getYearFromDate()
            view.tvPersonCharacterName.text = personMediaCast.character
            if (personMediaCast.posterPath != null) {
                glide.load(personMediaCast.posterPath).into(view.ivPersonMedia)
            } else {
                view.ivPersonMedia.setImageResource(R.drawable.ic_baseline_perm_media_24)
            }

            CoroutineScope(Dispatchers.IO).launch {
                personMediaCast.posterPath?.let {
                    view.personMediaCard.assignColors(
                        requireContext(),
                        it,
                        glide,
                        view.tvPersonMediaName,
                        view.tvPersonMediaRating,
                        view.tvPersonMediaReleaseDate,
                        view.tvAs,
                        view.tvPersonCharacterName
                    )
                }
            }

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_personDetailFragment_to_detailsFragment, personMediaCast.idBundle("SERIES"))
            }
        }
    }


    private fun setupPersonMoviesRecyclerView() {
        binding.rvPersonMovies.apply {
            adapter = personMoviesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            snapToChildView()
        }
    }

    private fun setupPersonTVShowsRecyclerView() {
        binding.rvPersonTVShows.apply {
            adapter = personTVShowsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            snapToChildView()
        }
    }

    private fun setToolbar(person: PersonDetails) {
        (activity as AppCompatActivity).apply {
            val toolbar = this.findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.title = person.name

            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setColors(person: PersonDetails) {
        person.profilePath?.let {
            binding.personDetailsBase.assignColors(
                requireContext(),
                it,
                glide,
                binding.tvPersonDetailName,
                binding.tvPersonBirthday,
                binding.tvPersonDeathDay,
                binding.tvPersonDetailBiography,
                binding.tvPersonKnownFor,
                binding.tvPersonPopularity,
                binding.tvPersonGender,
                binding.tvPersonBirthPlace,
                binding.tvBiography,
                binding.tvPersonMovies,
                binding.tvPersonTvShows
            )
        }
    }

    private fun setViews(person: PersonDetails) {
        if (person.profilePath != null) glide.load(person.profilePath).into(binding.ivPersonDetail)
        else binding.ivPersonDetail.setImageResource(R.drawable.ic_baseline_person_24)

        if (person.biography.isBlank()) binding.tvBiography.makeGone()
        if (person.birthday.isNullOrBlank()) binding.tvPersonBirthday.makeGone()
        if (person.deathDay.isNullOrBlank()) binding.tvPersonDeathDay.makeGone()
        if (person.gender == 0) binding.tvPersonGender.makeGone()
        if (person.placeOfBirth.isNullOrBlank()) binding.tvPersonBirthPlace.makeGone()

        binding.tvPersonDetailName.text = person.name
        binding.tvPersonBirthday.text = person.birthday
        binding.tvPersonDeathDay.text = person.deathDay
        binding.tvPersonDetailBiography.text = person.biography
        binding.tvPersonKnownFor.text = person.knownForDepartment.setAsKnownFor()
        binding.tvPersonPopularity.text = person.popularity.getBeautifulString()
        binding.tvPersonGender.text = person.gender.setAsGenderString()
        binding.tvPersonBirthPlace.text = person.placeOfBirth ?: ""
    }
}