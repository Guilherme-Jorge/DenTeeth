package br.edu.puccampinas.denteeth.emergencia

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.edu.puccampinas.denteeth.TelaPrincipalActivity
import br.edu.puccampinas.denteeth.classes.CustomResponse
import br.edu.puccampinas.denteeth.databinding.ActivityAtendimentoBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class AtendimentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAtendimentoBinding

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private val permissionCode = 101

    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    var clicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAtendimentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

        val telefone = intent.getStringExtra("telefone")
        binding.tvNumeroTelefone.text = telefone

        val nome = intent.getStringExtra("nome")
        binding.tvNomeSocorrista.text = "Ligue para $nome"

        binding.btncall.setOnClickListener {
            clicked = true
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$telefone")
            startActivity(intent)
        }

        binding.btnEnviarLocalizacao.setOnClickListener {
            sendLocation(
                currentLocation.latitude.toString(), currentLocation.longitude.toString(),
                intent.getStringExtra("fcmToken")!!
            ).addOnCompleteListener { res ->
                if (res.result.status == "SUCCESS") {
                    Snackbar.make(
                        binding.root,
                        "Localização enviada!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    val intentFinalizarAtendimento =
                        Intent(binding.root.context, FinalizarAtendimentoActivity::class.java)
                    intentFinalizarAtendimento.putExtra("fcmToken", intent.getStringExtra("fcmToken"))
                    this.startActivity(intentFinalizarAtendimento)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTemp.setText("" + millisUntilFinished / 1000)
                if (clicked == true) {
                    binding.tvTempoRestante.text = "Ligação concluída com sucesso!"
                    binding.tvTemp.setText("")
                    cancel()
                }
            }

            override fun onFinish() {
                binding.tvTemp.setText("Expirou")

                val intentreturn = Intent(binding.root.context, TelaPrincipalActivity::class.java)
                startActivity(intentreturn)

            }
        }.start()
    }

    private fun fetchLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), permissionCode
            )
            return
        }

        val task = fusedLocationProvider.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
            }
        }
    }

    private fun sendLocation(
        lat: String,
        lng: String,
        fcmToken: String
    ): Task<CustomResponse> {

        functions = Firebase.functions("southamerica-east1")

        val dadosProfissional = hashMapOf(
            "app" to "dentista",
            "titulo" to "Localização do dentista",
            "endereco" to "Vá até lá",
            "lat" to lat,
            "lng" to lng,
            "fcmToken" to fcmToken
        )

        return functions
            .getHttpsCallable("enviarDadosMapa")
            .call(dadosProfissional)
            .continueWith { task ->
                val result =
                    gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }
}