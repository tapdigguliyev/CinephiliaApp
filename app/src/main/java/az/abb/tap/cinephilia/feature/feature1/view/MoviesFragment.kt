package az.abb.tap.cinephilia.feature.feature1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.base.BaseAdapter
import az.abb.tap.cinephilia.databinding.FragmentMoviesBinding
import az.abb.tap.cinephilia.databinding.ItemMediaBinding

class MoviesFragment : Fragment() {
    private lateinit var binding: FragmentMoviesBinding
    private val topRatedMoviesAdapter by lazy { BaseAdapter<String>() }
    private val moviesAdapter by lazy { BaseAdapter<String>() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopRatedMoviesRecylerView()
        setupMoviesRecyclerView()
        setupTopRatedMoviesAdapter()
        setupMoviesAdapter()

        binding.tvName.setOnClickListener {
            findNavController().navigate(R.id.action_moviesFragment_to_movieDetailsFragment)
        }
    }

    private fun setupTopRatedMoviesAdapter() {
        topRatedMoviesAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemMediaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        topRatedMoviesAdapter.expressionOnBindViewHolder = { topRatedMovie, viewBinding ->
            val view = viewBinding as ItemMediaBinding
            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_moviesFragment_to_movieDetailsFragment)
            }
        }
    }

    private fun setupMoviesAdapter() {
        moviesAdapter.expressionOnCreateViewHolder = { viewGroup ->
            ItemMediaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        }

        moviesAdapter.expressionOnBindViewHolder = { movie, viewBinding ->
            val view = viewBinding as ItemMediaBinding
            view.root.setOnClickListener {
                findNavController().navigate(R.id.action_moviesFragment_to_movieDetailsFragment)
            }
        }
    }

    private fun setupMoviesRecyclerView() {
        binding.rvMovies.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupTopRatedMoviesRecylerView() {
        binding.rvTopRatedMovies.apply {
            adapter = topRatedMoviesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }
}