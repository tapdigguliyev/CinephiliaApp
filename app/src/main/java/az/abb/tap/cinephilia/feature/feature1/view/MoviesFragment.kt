package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import az.abb.tap.cinephilia.databinding.FragmentMoviesBinding

class MoviesFragment : Fragment() {
    private lateinit var binding: FragmentMoviesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}