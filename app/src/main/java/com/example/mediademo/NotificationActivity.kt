package com.example.mediademo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        //获取系统服务中的通知服务，并强制类型转换为通知管理器
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //兼容性判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建通知渠道，参数：id，通知渠道的名称，通知的重要等级（默认等级，高优先级，低优先级）
            val channel = NotificationChannel("normal", "测试通道", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        NotificationButton.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent, 0)
            val string = "万商超信--优质短信群发平台,采用短信发送成功计费、失败返款的模式,为企业提供106短信平台、短信接口、营销短信、通知短信、问候短信、短信服务平台、短信验证码等"
            //创建通知，参数：上下文，通知渠道id
            val notification = NotificationCompat.Builder(this, "normal")
                .setContentTitle("我是标题")
                .setContentText(string)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.dog_1))
                        .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.cat_3))
                )
                .setSmallIcon(R.drawable.cat_3)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.dog_1))
                .setContentIntent(pi)
                //.setAutoCancel(true)
                .build()
            //发出通知，参数，通知id，通知对象名
            manager.notify(1, notification)
        }
    }
}