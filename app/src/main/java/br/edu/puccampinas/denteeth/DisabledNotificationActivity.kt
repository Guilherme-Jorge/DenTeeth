package br.edu.puccampinas.denteeth

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import br.edu.puccampinas.denteeth.databinding.ActivityDisabledNotificationBinding

class DisabledNotificationActivity : AppCompatActivity() {

    lateinit var binding: ActivityDisabledNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDisabledNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAbrirConfig.setOnClickListener {
            val intentNotification = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intentNotification.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(intentNotification)
        }

        binding.btnVoltar.setOnClickListener {
            val intentTelaInicio = Intent(this, TelaInicioActivity::class.java)
            startActivity(intentTelaInicio)
        }
    }
}