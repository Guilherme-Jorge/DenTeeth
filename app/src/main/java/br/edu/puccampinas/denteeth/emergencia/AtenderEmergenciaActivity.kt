package br.edu.puccampinas.denteeth.emergencia

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import br.edu.puccampinas.denteeth.R
import br.edu.puccampinas.denteeth.databinding.ActivityAtenderEmergenciaBinding

class AtenderEmergenciaActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAtenderEmergenciaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityAtenderEmergenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbNavbar)

        val navController = findNavController(R.id.nav_host_fragment_content_lista_emergencia)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_lista_emergencia)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun copyToClipboard(text: String?) {
        val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("telefone", text)
        cm.setPrimaryClip(clipData)
    }
}