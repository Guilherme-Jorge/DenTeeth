package br.edu.puccampinas.denteeth.conta

import android.Manifest
import android.app.usage.NetworkStats
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.telephony.NetworkScan
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import br.edu.puccampinas.denteeth.R
import br.edu.puccampinas.denteeth.TelaInicioActivity
import br.edu.puccampinas.denteeth.databinding.ActivityCriarContaBinding
import br.edu.puccampinas.denteeth.datastore.UserPreferencesRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.remote.ConnectivityMonitor.NetworkStatus
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import java.util.Objects

class CriarContaActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binding: ActivityCriarContaBinding
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        userPreferencesRepository = UserPreferencesRepository.getInstance(this)

        binding = ActivityCriarContaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbNavbar)

        navController = findNavController(R.id.nav_host_fragment_content_criar_conta)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {

        val navController = findNavController(R.id.nav_host_fragment_content_criar_conta)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun hideSoftKeyboard(v: View) {

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun goToInicio() {
        val intentTelaInicio = Intent(this, TelaInicioActivity::class.java)

        this.startActivity(intentTelaInicio)
    }

    fun storeUserId(uid: String){
        userPreferencesRepository.uid = uid
        userPreferencesRepository.updateUid(uid)
    }

    fun getFcmToken(): String{
        return userPreferencesRepository.fcmToken
    }
}