package br.edu.puccampinas.denteeth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.edu.puccampinas.denteeth.databinding.FragmentTermosDeUsoBinding
import com.google.firebase.functions.FirebaseFunctionsException

class TermosDeUsoFragment : Fragment() {

    private var _binding: FragmentTermosDeUsoBinding? = null
    private val binding get() = _binding!!
    private val TAG = "DenTeeth Firebase"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTermosDeUsoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.btnAceitarTermos.setOnClickListener {
//            (activity as CriarContaActivity).adcionarProfissional()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d(TAG, "Usuário salvo com sucesso: ${task.result}")
//                        Toast.makeText(
//                            binding.root.context,
//                            "Usuário salvo com sucesso",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    } else {
//                        val e = task.exception
//                        if (e is FirebaseFunctionsException) {
//                            Log.e(TAG, "Erro ao salvar usuário: [${e.code}]")
//                        } else {
//                            Log.w(TAG, "Erro desconhecido: ${task.exception}")
//                            Toast.makeText(
//                                binding.root.context,
//                                "Usuário salvo com sucesso",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                }
        }
    }
}