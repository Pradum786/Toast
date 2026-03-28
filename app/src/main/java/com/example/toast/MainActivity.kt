package com.example.toast

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pradum.flashToast.FlashToast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.success).setOnClickListener {
            FlashToast.success(
                this,
                "Message 1 - This will show first",
                FlashToast.LONG
            )
        }

        findViewById<Button>(R.id.error).setOnClickListener {
            FlashToast.error(
                this,
                "Message 1 - This will show first",
                FlashToast.LONG
            )
        }

        findViewById<Button>(R.id.warning).setOnClickListener {
            FlashToast.warning(
                this,
                "Message 1 - This will show first",
                FlashToast.LONG
            )
        }

        findViewById<Button>(R.id.info).setOnClickListener {
            FlashToast.info(
                this,
                "Message 1 - This will show first",
                FlashToast.LONG
            )
        }

    }
}