package com.example.mediademo

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        //获取系统服务中的通知服务，并强制类型转换为通知管理器
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(1)

//        val translateAnimation = TranslateAnimation(0F, 200F, 0F, 0F)
//        translateAnimation.duration = 2000
//        translateAnimation.repeatCount = 5
//        translateAnimation.repeatMode = 2
//        textView.animation = translateAnimation
//        translateAnimation.start()

        val anim = AnimationUtils.loadAnimation(this, R.anim.translate_anim)
        anim.interpolator = LinearInterpolator()
        textView.animation = anim
        anim.start()

        button2.setOnClickListener {
//            translateAnimation.cancel()
            anim.cancel()
        }
    }
}