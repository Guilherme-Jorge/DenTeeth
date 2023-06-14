package br.edu.puccampinas.denteeth.classes

import com.google.firebase.Timestamp

data class Emergencia(val dataHora: Timestamp?, val descricao: String?, val fotos1: String?, val fotos2: String?, val fotos3: String?, val id: String?, val nome: String?, val status: String?, val telefone: String?)
