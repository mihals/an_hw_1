package ru.netology.nmedia.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.AppActivityBinding
import ru.netology.nmedia.viewModel.PostViewModel

class AppActivity:AppCompatActivity(R.layout.app_activity){
    private val viewModel: PostViewModel by viewModels()
}