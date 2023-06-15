package br.edu.puccampinas.denteeth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.puccampinas.denteeth.adapter.AvaliacoesAdapter
import br.edu.puccampinas.denteeth.adapter.EmergenciasAdapter
import br.edu.puccampinas.denteeth.classes.Avaliacao
import br.edu.puccampinas.denteeth.classes.Emergencia
import br.edu.puccampinas.denteeth.databinding.FragmentReputacaoBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the third destination in the navigation.
 */
class ReputacaoFragment : Fragment() {

    private var _binding: FragmentReputacaoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var avaliacoesAdapter: AvaliacoesAdapter
    private val db = Firebase.firestore
    private var allAvaliacoes = ArrayList<Avaliacao>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentReputacaoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allAvaliacoes.clear()
        avaliacoesAdapter = AvaliacoesAdapter(allAvaliacoes)

        _binding!!.rvReputacaoAvaliacao.layoutManager = LinearLayoutManager(binding.root.context)
        _binding!!.rvReputacaoAvaliacao.adapter = avaliacoesAdapter
    }

    override fun onStart() {
        super.onStart()

        loadAvaliacoes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadAvaliacoes() {
        val doc = db.collection("avaliacao").orderBy("dataHora", Query.Direction.DESCENDING)
        var avaliacao: Avaliacao
        doc.addSnapshotListener { value, e ->
            allAvaliacoes.clear()
            if (e != null) {
                Log.e("FirestoreListener", "Leitura falhou", e)
                return@addSnapshotListener
            }

            for (document in value!!) {
                avaliacao = Avaliacao(
                    document.data["dataHora"] as Timestamp,
                    document.data["fcmToken"].toString(),
                    document.data["notaApp"].toString().toInt(),
                    document.data["notaAvaliacao"].toString().toInt(),
                    document.data["profissional"].toString(),
                    document.data["textoApp"].toString(),
                    document.data["textoAvaliacao"].toString()
                )

                allAvaliacoes.add(avaliacao)
            }

            avaliacoesAdapter.notifyDataSetChanged()
        }
    }
}