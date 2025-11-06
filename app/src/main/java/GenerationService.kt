package com.example.test

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Button
import androidx.core.app.NotificationCompat
import kotlin.random.Random



class GenerationService : Service() {
    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANEL_ID = "chanelID"
    }


    override fun onCreate () {
        super.onCreate()

        val chanel = NotificationChannel(
            CHANEL_ID,
            "Generation",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(chanel)


    }

    override fun onStartCommand (intent: Intent?, flags: Int, startId: Int): Int {

        val builder = NotificationCompat.Builder(this, CHANEL_ID)
            .setContentTitle("FYI")
            .setContentText("Generation is in progress")
            .setSmallIcon(R.drawable.notify)

        startForeground(NOTIFICATION_ID, builder.build())

        Thread {
            val (generatedLength, overallTimeSec) = generatePassword()

            val notificationCompleted = NotificationCompat.Builder(this, CHANEL_ID)
                .setContentTitle("Generation completed")
                .setContentText("Generated Length: $generatedLength\nTime: $overallTimeSec")
                .setSmallIcon(R.drawable.notify)
                .build()


            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.notify(NOTIFICATION_ID, notificationCompleted)
        }.start()





        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    fun generatePassword(): Pair<Int, Double> {
        val startTime = System.currentTimeMillis()


        val passwords = mutableListOf<String>()
        var generated = 0


        while (generated < 120){
            var temp = ""
            val passwordLength = Random.nextInt(8,18)

            for (i in 0 until passwordLength){
                var randomInt = Random.nextInt(0,10)
                temp += randomInt
            }
            passwords.add(temp)

            generated +=1
            Thread.sleep(1000)


        }
        val overallTimeSec = (System.currentTimeMillis() - startTime) / 1000.00
        val generatedLength = passwords.sumOf { it.length}


        return Pair(generatedLength, overallTimeSec)
    }
}