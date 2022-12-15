package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import az.abb.tap.cinephilia.databinding.FragmentPeopleBinding
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeopleFragment : Fragment() {
    private lateinit var binding: FragmentPeopleBinding

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPeopleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}