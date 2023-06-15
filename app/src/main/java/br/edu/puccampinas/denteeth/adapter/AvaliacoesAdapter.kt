package br.edu.puccampinas.denteeth.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.edu.puccampinas.denteeth.R
import br.edu.puccampinas.denteeth.ReputacaoRevisaoActivity
import br.edu.puccampinas.denteeth.classes.Avaliacao
import br.edu.puccampinas.denteeth.classes.Emergencia
import br.edu.puccampinas.denteeth.emergencia.AtenderEmergenciaActivity
import com.bumptech.glide.Glide

class AvaliacoesAdapter(private val dataSet: List<Avaliacao>) : ListAdapter<Avaliacao, AvaliacoesAdapter.AvaliacaoViewHolder>(AvaliacaoDiffCallback) {
    class AvaliacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTextoNota: TextView = itemView.findViewById(R.id.tvTextoNota)
        private val tvNota: TextView = itemView.findViewById(R.id.tvNota)
        private var avaliacaoCurrent: Avaliacao? = null

        fun bind(a: Avaliacao) {
            avaliacaoCurrent = a
            tvTextoNota.text = a.textoAvaliacao
            tvNota.text = a.notaAvaliacao.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvaliacaoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.avaliacao_list_item, parent, false)
        return AvaliacaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvaliacaoViewHolder, position: Int) {
        val t = dataSet[position]
        holder.bind(t)

        holder.itemView.setOnClickListener {
            val intentAvaliacao = Intent(it.context, ReputacaoRevisaoActivity::class.java)
            intentAvaliacao.putExtra("fcmToken", t.fcmToken)
            intentAvaliacao.putExtra("notaApp", t.notaApp.toString())
            intentAvaliacao.putExtra("notaAvaliacao", t.notaAvaliacao.toString())
            intentAvaliacao.putExtra("profissional", t.profissional)
            intentAvaliacao.putExtra("textoApp", t.textoApp)
            intentAvaliacao.putExtra("textoAvaliacao", t.textoAvaliacao)
            intentAvaliacao.putExtra("dataHora", t.dataHora!!.toDate().toInstant().toString())

            it.context.startActivity(intentAvaliacao)
        }
    }

    override fun getItemCount() = dataSet.size
}

object AvaliacaoDiffCallback : DiffUtil.ItemCallback<Avaliacao>() {
    override fun areItemsTheSame(oldItem: Avaliacao, newItem: Avaliacao): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Avaliacao, newItem: Avaliacao): Boolean {
        return oldItem.textoAvaliacao == newItem.textoAvaliacao
    }
}