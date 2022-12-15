package az.abb.tap.cinephilia.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navHostFragment.findNavController().addOnDestinationChangedListener {_, destination, _ ->
            when(destination.id) {
                R.id.moviesFragment, R.id.seriesFragment, R.id.peopleFragment -> supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(false)
                    supportActionBar?.title = this@MainActivity.getString(R.string.app_name)
                }
                else -> supportActionBar?.apply { setDisplayHomeAsUpEnabled(true) }
            }
        }
    }
}