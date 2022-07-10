package ru.netology.nmedia.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.AppActivityBinding

class AppActivity:AppCompatActivity(R.layout.app_activity){
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val oc=true
    }

    override fun onDestroy() {
        super.onDestroy()
        val od=true
    }
}