package br.edu.puccampinas.denteeth

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import br.edu.puccampinas.denteeth.databinding.ActivityCriarContaBinding
import br.edu.puccampinas.denteeth.datastore.UserPreferencesRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions

class CriarContaActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCriarContaBinding
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        userPreferencesRepository = UserPreferencesRepository.getInstance(this)

        binding = ActivityCriarContaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbNavbar)

        val navController = findNavController(R.id.nav_host_fragment_content_criar_conta)
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

    fun storeUserId(uid: String){
        userPreferencesRepository.uid = uid
    }

    fun getFcmToken(): String{
        return userPreferencesRepository.fcmToken
    }

//    fun adcionarProfissional(): Task<String> {
//
//        val fragmentRegistro = RegistroFragment()
//
//        return FirebaseFunctions
//            .getInstance("southamerica-east1")
//            .getHttpsCallable("salvarDadosPessoais")
//            .call(fragmentRegistro.dadosProfissional())
//            .continueWith { task ->
//                val result = task.result?.data as String
//                result
//            }
//    }
}