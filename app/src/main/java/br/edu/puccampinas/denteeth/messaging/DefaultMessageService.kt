package br.edu.puccampinas.denteeth.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import br.edu.puccampinas.denteeth.classes.CustomResponse
import br.edu.puccampinas.denteeth.R
import br.edu.puccampinas.denteeth.datastore.UserPreferencesRepository
import br.edu.puccampinas.denteeth.emergencia.AtenderEmergenciaActivity
import br.edu.puccampinas.denteeth.emergencia.AtendimentoActivity
import br.edu.puccampinas.denteeth.emergencia.MapsActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.GsonBuilder

class DefaultMessageService : FirebaseMessagingService() {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val msgData = remoteMessage.data
        Log.d("Teste", msgData.toString())
        if (msgData["type"].toString() == "novaEmergencia") {
            val msg = msgData["descricao"].toString()
            showNotificationEmergencia(msg, msgData)
        } else if (msgData["type"].toString() == "mapa") {
            val msg = msgData["text"].toString()
            showNotificationMapa(msg, msgData)
        } else if (msgData["type"].toString() == "telefone") {
            val msg = msgData["texto"].toString()
            showNotificationTelefone(msg, msgData)
        } else {
            //
        }
    }

    override fun onNewToken(token: String) {
        Log.d("DefaultMessageService", "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun updateTokenProfissional(token: String?, uid: String): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        val dadosUsuario = hashMapOf(
            "fcmToken" to token,
            "uid" to uid
        )

        return functions
            .getHttpsCallable("updateTokenProfissional")
            .call(dadosUsuario)
            .continueWith { task ->
                val result =
                    gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }

    fun sendRegistrationToServer(token: String?) {
        // Guardar apenas no DataStore. Vamos manipular isso sempre no Login ou criação de conta.
        userPreferencesRepository = UserPreferencesRepository.getInstance(this)
        userPreferencesRepository.updateFcmToken(token!!)

        Log.d("DefaultMessageService", "sendRegistrationTokenToServer($token)")
        updateTokenProfissional(token, userPreferencesRepository.uid).addOnCompleteListener { res ->
            if (res.result.status == "SUCCESS") {
                Log.d("DefaultMessageService", "FCMToken successfully updated.")
            } else {
                Log.e("DefaultMessageService", "Error while updating FCMToken.")
            }
        }
    }

    private fun showNotificationEmergencia(messageBody: String, messageData: Map<String, String>) {
        val intent = Intent(this, AtenderEmergenciaActivity::class.java)
        intent.action = "actionstring" + System.currentTimeMillis()
        intent.putExtra("nome", messageData["nome"].toString())
        intent.putExtra("fotos1", messageData["fotos1"].toString())
        intent.putExtra("fotos2", messageData["fotos2"].toString())
        intent.putExtra("fotos3", messageData["fotos3"].toString())
        intent.putExtra("status", messageData["status"].toString())
        intent.putExtra("descricao", messageData["descricao"].toString())
        intent.putExtra("id", messageData["id"].toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_tooth)
            .setContentTitle(getString(R.string.fcm_default_title_message))
            .setContentText(messageBody)
            .setColor(ContextCompat.getColor(this, R.color.blue_main))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun showNotificationTelefone(messageBody: String, messageData: Map<String, String>) {
        val intent = Intent(this, AtendimentoActivity::class.java)
        intent.action = "actionstring" + System.currentTimeMillis()
        intent.putExtra("telefone", messageData["telefone"].toString())
        intent.putExtra("fcmToken", messageData["fcmToken"].toString())
        intent.putExtra("nome", messageData["nome"].toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_tooth)
            .setContentTitle(getString(R.string.fcm_secondary_title_message))
            .setContentText(messageBody)
            .setColor(ContextCompat.getColor(this, R.color.blue_main))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun showNotificationMapa(messageBody: String, messageData: Map<String, String>) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.action = "actionstring" + System.currentTimeMillis()
        intent.putExtra("lat", messageData["lat"].toString())
        intent.putExtra("lng", messageData["lng"].toString())
        intent.putExtra("titulo", messageData["titulo"].toString())
        intent.putExtra("endereco", messageData["endereco"].toString())
        intent.putExtra("fcmToken", messageData["fcmToken"].toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_tooth)
            .setContentTitle(getString(R.string.fcm_tertiary_title_message))
            .setContentText(messageBody)
            .setColor(ContextCompat.getColor(this, R.color.blue_main))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}
