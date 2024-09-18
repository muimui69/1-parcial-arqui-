package com.example.personal_training

import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.personal_training.databinding.ActivityMainBinding
import com.example.personal_training.db.DatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbHelper = DatabaseHelper(this)
        dbHelper.writableDatabase

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setupFabNavigation(navController)
    }

    private fun setupFabNavigation(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home -> {
                    binding.appBarMain.fab.show()
                    binding.appBarMain.fab.setOnClickListener {
                        binding.appBarMain.fab.hide()
                        navController.navigate(R.id.action_nav_clientes_to_agregarClienteFragment)
                    }
                }
                R.id.nav_gallery -> {
                    binding.appBarMain.fab.show()
                    binding.appBarMain.fab.setOnClickListener {
                        Snackbar.make(it, "Añadir Rutina", Snackbar.LENGTH_LONG).show()
                    }
                }
                R.id.nav_slideshow -> {
                    binding.appBarMain.fab.show()
                    binding.appBarMain.fab.setOnClickListener {
                        Snackbar.make(it, "Añadir Dieta", Snackbar.LENGTH_LONG).show()
                    }
                }
                else -> {
                    binding.appBarMain.fab.hide()  // Ocultar FAB si no hay acción específica
                    binding.appBarMain.fab.setOnClickListener(null)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
