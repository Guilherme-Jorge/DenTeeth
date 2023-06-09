package br.edu.puccampinas.denteeth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.google.android.material.button.MaterialButton

class DisabledNotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disabled_notification)

        val btnAbrirConfig: MaterialButton = findViewById(R.id.btnAbrirConfig)
        btnAbrirConfig.setOnClickListener {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(intent)
        }

    }
}