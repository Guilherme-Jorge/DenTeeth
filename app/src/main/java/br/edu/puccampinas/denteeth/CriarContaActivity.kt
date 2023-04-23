package br.edu.puccampinas.denteeth

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import br.edu.puccampinas.denteeth.databinding.ActivityCriarContaBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions

class CriarContaActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCriarContaBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

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

//    fun dadosProfissional(): HashMap<String, String> {
//
//        return hashMapOf(
//            "nome" to binding.etNome.text.toString(),
//            "telefone" to binding.etTelefone.text.toString(),
//            "email" to binding.etEmail.text.toString(),
//            "endereco1" to binding.etEndereco1.text.toString(),
//            "curriculo" to binding.etCurriculo.text.toString()
//        )
//    }

    fun adcionarProfissional(): Task<String> {

        return FirebaseFunctions
            .getInstance("southamerica-east1")
            .getHttpsCallable("salvarDadosPessoais")
            .call()
            .continueWith { task ->
                val result = task.result?.data as String
                result
            }
    }
}