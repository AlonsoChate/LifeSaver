package com.example.ve441_lifesaver_draft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun visitMaps(view: View?) = startActivity(Intent(this, MapsActivity::class.java))
    fun visitCPR(view: View?) = startActivity(Intent(this, CPRs1Activity::class.java))
}