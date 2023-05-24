package br.edu.puccampinas.denteeth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import br.edu.puccampinas.denteeth.adapter.EmergenciasAdapter
import br.edu.puccampinas.denteeth.classes.Emergencia
import br.edu.puccampinas.denteeth.databinding.FragmentListaEmergenciasBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListaEmergenciasFragment : Fragment() {

    private var _binding: FragmentListaEmergenciasBinding? = null
    private val binding get() = _binding!!
    private lateinit var emergeciasAdapter: EmergenciasAdapter
    private val db = Firebase.firestore
    private var allEmergencias = ArrayList<Emergencia>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListaEmergenciasBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allEmergencias.clear()
        emergeciasAdapter = EmergenciasAdapter(allEmergencias)

        _binding!!.rvEmergencias.layoutManager = GridLayoutManager(binding.root.context, 2)
        _binding!!.rvEmergencias.adapter = emergeciasAdapter
    }

    override fun onStart() {
        super.onStart()

        loadEmergencias()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadEmergencias() {
        val doc = db.collection("emergencias").orderBy("dataHora")
        var emergencia: Emergencia
        doc.addSnapshotListener { value, e ->
            allEmergencias.clear()
            if (e != null) {
                Log.e("FirestoreListener", "Leitura falhou", e)
                return@addSnapshotListener
            }

            for (document in value!!) {
                emergencia = Emergencia(
                    document.data["dataHora"].toString(),
                    document.data["descricao"].toString(),
                    document.data["fotos"].toString(),
                    document.data["id"].toString(),
                    document.data["nome"].toString(),
                    document.data["status"].toString(),
                    document.data["telefone"].toString()
                )

                allEmergencias.add(emergencia)
            }

            emergeciasAdapter.notifyDataSetChanged()
        }
    }
}