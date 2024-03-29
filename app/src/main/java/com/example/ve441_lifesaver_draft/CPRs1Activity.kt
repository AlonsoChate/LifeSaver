package com.example.ve441_lifesaver_draft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class CPRs1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cprs1)
    }

    fun returnHome(view: View?) = startActivity(Intent(this, MainActivity::class.java))
    fun nextPage(view: View?) = startActivity(Intent(this, CPRs2Activity::class.java))
}