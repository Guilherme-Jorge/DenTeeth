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
import br.edu.puccampinas.denteeth.classes.Emergencia
import br.edu.puccampinas.denteeth.emergencia.AtenderEmergenciaActivity
import com.bumptech.glide.Glide

class EmergenciasAdapter(private val dataSet: List<Emergencia>) : ListAdapter<Emergencia, EmergenciasAdapter.EmergenciaViewHolder>(EmergenciaDiffCallback) {
    class EmergenciaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvEmergenciaTempo: TextView = itemView.findViewById(R.id.tvEmergenciaTempo)
        private val ivEmergenciaFoto: ImageView = itemView.findViewById(R.id.ivEmergenciaFoto)
        private var emergenciaCurrent: Emergencia? = null

        fun bind(e: Emergencia) {
            emergenciaCurrent = e
            tvEmergenciaTempo.text = e.dataHora.toString()
            Glide.with(ivEmergenciaFoto.context).load(e.fotos.toString()).into(ivEmergenciaFoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergenciaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.emergencia_list_item, parent, false)
        return EmergenciaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmergenciaViewHolder, position: Int) {
        val t = dataSet[position]
        holder.bind(t)

        holder.itemView.setOnClickListener {
            val intentEmergencia = Intent(it.context, AtenderEmergenciaActivity::class.java)
//            intentEmergencia.action = "actionstring" + System.currentTimeMillis()
            intentEmergencia.putExtra("nome", t.nome.toString())
            intentEmergencia.putExtra("telefone", t.telefone.toString())
            intentEmergencia.putExtra("fotos", t.fotos.toString())
            intentEmergencia.putExtra("status", t.status.toString())
            intentEmergencia.putExtra("descricao", t.descricao.toString())
            intentEmergencia.putExtra("dataHora", t.dataHora.toString())
            intentEmergencia.putExtra("id", t.id.toString())

            it.context.startActivity(intentEmergencia)
        }
    }

    override fun getItemCount() = dataSet.size
}

object EmergenciaDiffCallback : DiffUtil.ItemCallback<Emergencia>() {
    override fun areItemsTheSame(oldItem: Emergencia, newItem: Emergencia): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Emergencia, newItem: Emergencia): Boolean {
        return oldItem.fotos.toString() == newItem.fotos.toString()
    }
}