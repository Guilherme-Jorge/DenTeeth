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
import br.edu.puccampinas.denteeth.R
import br.edu.puccampinas.denteeth.datastore.UserPreferencesRepository
import br.edu.puccampinas.denteeth.emergencia.EmergenciaFragment
import br.edu.puccampinas.denteeth.emergencia.ListaEmergenciaActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DefaultMessageService : FirebaseMessagingService() {

    private lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val msgData = remoteMessage.data
        val msg = msgData["descricao"]
        showNotification(msg!!, msgData)
    }

    override fun onNewToken(token: String) {
        Log.d("DefaultMessageService", "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    /**
     * Atualizar o FCM token caso tenha mudado.
     * Sem implementação para este exemplo.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d("DefaultMessageService", "sendRegistrationTokenToServer($token)")
        // Guardar apenas no DataStore. Vamos manipular isso sempre no Login ou criação de conta.
        userPreferencesRepository = UserPreferencesRepository.getInstance(this)
        userPreferencesRepository.updateFcmToken(token!!)

    }

    private fun showNotification(messageBody: String, messageData: Map<String, String>) {
        val intent = Intent(this, ListaEmergenciaActivity::class.java)
        intent.putExtra("nome", messageData["nome"])
        intent.putExtra("telefone", messageData["telefone"])
        intent.putExtra("fotos", messageData["fotos"])
        intent.putExtra("status", messageData["status"])
        intent.putExtra("descricao", messageData["descricao"])
        intent.putExtra("dataHora", messageData["dataHora"])
        intent.putExtra("id", messageData["id"])
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE)
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
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}
