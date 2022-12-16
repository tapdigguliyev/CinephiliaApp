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
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.databinding.FragmentPersonDetailBinding
import az.abb.tap.cinephilia.feature.feature1.model.persondetails.PersonDetails
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

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPersonDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPersonDetails(args.personId)
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
            binding.svPersonMainDetails.assignColors(
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
                binding.tvBiography
            )
        }
    }

    private fun setViews(person: PersonDetails) {
        if (person.profilePath != null) glide.load(person.profilePath).into(binding.ivPersonDetail)
        else binding.ivPersonDetail.setImageResource(R.drawable.ic_baseline_person_24)

        if(person.biography.isEmpty()) binding.tvBiography.makeInvisible()

        binding.tvPersonDetailName.text = person.name
        binding.tvPersonBirthday.text = person.birthday ?: ""
        binding.tvPersonDeathDay.text = person.deathDay ?: ""
        binding.tvPersonDetailBiography.text = person.biography
        binding.tvPersonKnownFor.text = person.knownForDepartment.setAsKnownFor()
        binding.tvPersonPopularity.text = person.popularity.getBeautifulString()
        binding.tvPersonGender.text = person.gender.setAsGenderString()
        binding.tvPersonBirthPlace.text = person.placeOfBirth ?: ""
    }
}