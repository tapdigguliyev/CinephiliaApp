package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            val toolbar = this.findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.title = "Movie Details Fragment"

            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}