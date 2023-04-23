package br.edu.puccampinas.denteeth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.puccampinas.denteeth.databinding.FragmentRegistroBinding
import com.google.android.material.snackbar.Snackbar

class RegistroFragment : Fragment() {

    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmarDados.setOnClickListener {
            (activity as CriarContaActivity).hideSoftKeyboard(binding.btnConfirmarDados)

            if (binding.etNome.text?.isEmpty() == true ||
                binding.etEmail.text?.isEmpty() == true ||
                binding.etSenha.text?.isEmpty() == true ||
                binding.etTelefone.text?.isEmpty() == true ||
                binding.etEndereco1.text?.isEmpty() == true ||
                binding.etCurriculo.text?.isEmpty() == true
            ) {
                Snackbar.make(binding.root, "Há campos obrigatórios vazios.", Snackbar.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (binding.etCurriculo.text.toString().length > 2000) {
                Snackbar.make(
                    binding.root,
                    "Currículo com mais de 2000 caracteres.",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                return@setOnClickListener
            }

            findNavController().navigate(R.id.action_RegistroFragment_to_TelaCameraFragment)
        }
    }
}