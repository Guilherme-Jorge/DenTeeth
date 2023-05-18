package br.edu.puccampinas.denteeth

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import br.edu.puccampinas.denteeth.databinding.ActivityTelaPrincipalBinding
import br.edu.puccampinas.denteeth.emergencia.EmergenciaFragment
import br.edu.puccampinas.denteeth.emergencia.SecondFragment

class TelaPrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTelaPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(EmergenciaFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_emergencia -> {
                    replaceFragment(FirstFragment())
                    true
                }
                R.id.item_perfil -> {
                    replaceFragment(Second2Fragment())
                    true
                }
                R.id.item_reputacao -> {
                    replaceFragment(ThirdFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun replaceFragment (fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.include, fragment)
        fragmentTransaction.commit()
    }
}