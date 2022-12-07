package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import az.abb.tap.cinephilia.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
}