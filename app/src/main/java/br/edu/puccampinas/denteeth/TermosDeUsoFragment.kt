package br.edu.puccampinas.denteeth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.edu.puccampinas.denteeth.databinding.FragmentTermosDeUsoBinding
import com.google.android.material.snackbar.Snackbar
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
            (activity as CriarContaActivity).goToInicio()
        }

        binding.btnRecusarTermos.setOnClickListener {
            Snackbar.make(
                requireView(),
                "E necessario aceitar os termos para criar a conta!",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}