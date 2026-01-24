package com.example.mytenthprojectv50

import dagger.hilt.android.AndroidEntryPoint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mytenthprojectv50.todolist.ui.screen.AppContent
import com.example.mytenthprojectv50.todolist.ui.screen.MainScreen
import com.example.mytenthprojectv50.ui.theme.MyTenthProjectV50Theme
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTenthProjectV50Theme {
                AppContent()
            }
        }
    }
}

