package com.example.ve441_lifesaver_draft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class CPRs2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cprs2)
    }
    fun startAR(view: View?) = startActivity(Intent(this, ARActivity::class.java))
    fun returnHome(view: View?) = startActivity(Intent(this, MainActivity::class.java))
    fun nextPage(view: View?) = startActivity(Intent(this, CPRs3Activity::class.java))
    fun prevPage(view: View?) = startActivity(Intent(this, CPRs1Activity::class.java))
}