package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.base.BasePagingAdapter
import az.abb.tap.cinephilia.databinding.FragmentPeopleBinding
import az.abb.tap.cinephilia.databinding.ItemPersonBinding
import az.abb.tap.cinephilia.feature.feature1.model.person.Person
import az.abb.tap.cinephilia.feature.feature1.viewmodel.MainViewModel
import az.abb.tap.cinephilia.utility.*
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PeopleFragment : Fragment() {
    private lateinit var binding: FragmentPeopleBinding
    private val viewModel: MainViewModel by viewModels()
    private val trendingPeopleAdapter by lazy { BasePagingAdapter<Person>() }
    private val popularPeopleAdapter by lazy { BasePagingAdapter<Person>() }

    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            observeTrendingPeople()
            observePopularPeople()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPeopleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTrendingPeopleRecyclerView()
        setupPopularPeopleRecyclerView()
        setupTrendingPeopleAdapter()
        setupPopularPeopleAdapter()
    }

    private fun observeTrendingPeople() {
        lifecycleScope.launch {
            viewModel.getTrendingPeople().observe(viewLifecycleOwner) {
                it?.let {
                    val trendingPersonPagingData = it.map { result ->
                        result.toPerson()
                    }
                    trendingPeopleAdapter.submitData(lifecycle, trendingPersonPagingData)
                }
            }
        }
    }

    private fun observePopularPeople() {
        lifecycleScope.launch {
            viewModel.getPopularPeople().observe(viewLifecycleOwner) {
                it?.let {
                    val popularPersonPagingData = it.map { result ->
                        result.toPerson()
                    }
                    popularPeopleAdapter.submitData(lifecycle, popularPersonPagingData)
                }
            }
        }
    }

    private fun setupTrendingPeopleAdapter() {
        trendingPeopleAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemPersonBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        trendingPeopleAdapter.expressionOnBindViewHolder = { trendingPerson, viewBinding ->
            val view = viewBinding as ItemPersonBinding

            view.tvPersonName.text = trendingPerson.name
            view.tvKnownFor.text = trendingPerson.knownForDepartment.setAsKnownFor()
            view.tvPopularity.text = trendingPerson.popularity.getBeautifulString()
            view.tvGender.text = trendingPerson.gender.setAsGenderString()

            if (trendingPerson.profilePath != null) {
                glide.load(trendingPerson.profilePath).into(view.ivPerson)
            } else {
                view.ivPerson.setImageResource(R.drawable.ic_baseline_person_24)
            }

            CoroutineScope(Dispatchers.IO).launch {
                trendingPerson.profilePath?.let {
                    view.personItemCard.assignColors(
                        requireContext(),
                        it,
                        glide,
                        view.tvPersonName,
                        view.tvKnownFor,
                        view.tvPopularity,
                        view.tvGender
                    )
                }
            }

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_peopleFragment_to_personDetailFragment, trendingPerson.idBundle())
            }
        }

        trendingPeopleAdapter.addLoadStateListener { loadState ->
            loadState.setup(requireContext(), binding.pbTrendingPeople)
        }
    }

    private fun setupPopularPeopleAdapter() {
        popularPeopleAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemPersonBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        popularPeopleAdapter.expressionOnBindViewHolder = { popularPerson, viewBinding ->
            val view = viewBinding as ItemPersonBinding

            view.tvPersonName.text = popularPerson.name
            view.tvKnownFor.text = popularPerson.knownForDepartment.setAsKnownFor()
            view.tvPopularity.text = popularPerson.popularity.getBeautifulString()
            view.tvGender.text = popularPerson.gender.setAsGenderString()

            if (popularPerson.profilePath != null) {
                glide.load(popularPerson.profilePath).into(view.ivPerson)
            } else {
                view.ivPerson.setImageResource(R.drawable.ic_baseline_person_24)
            }

            CoroutineScope(Dispatchers.IO).launch {
                popularPerson.profilePath?.let {
                    view.personItemCard.assignColors(
                        requireContext(),
                        it,
                        glide,
                        view.tvPersonName,
                        view.tvKnownFor,
                        view.tvPopularity,
                        view.tvGender
                    )
                }
            }

            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_peopleFragment_to_personDetailFragment, popularPerson.idBundle())
            }
        }

        popularPeopleAdapter.addLoadStateListener { loadState ->
            loadState.setup(requireContext(), binding.pbPopularPeople)
        }
    }

    private fun setupTrendingPeopleRecyclerView() {
        binding.rvTrendingPeople.apply {
            adapter = trendingPeopleAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            snapToChildView()
        }
    }

    private fun setupPopularPeopleRecyclerView() {
        binding.rvPopularPeople.apply {
            adapter = popularPeopleAdapter
            layoutManager = LinearLayoutManager(requireContext())
            snapToChildView()
        }
    }
}