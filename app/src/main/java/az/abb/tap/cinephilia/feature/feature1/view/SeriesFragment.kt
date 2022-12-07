package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import az.abb.tap.cinephilia.databinding.FragmentSeriesBinding

class SeriesFragment : Fragment() {
    private lateinit var binding: FragmentSeriesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }
}