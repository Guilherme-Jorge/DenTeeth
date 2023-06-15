package br.edu.puccampinas.denteeth.classes

import com.google.firebase.Timestamp

data class Avaliacao(val dataHora: Timestamp?, val fcmToken: String, val notaApp: Number, val notaAvaliacao: Number, val profissional: String, val textoApp: String, val textoAvaliacao: String)
