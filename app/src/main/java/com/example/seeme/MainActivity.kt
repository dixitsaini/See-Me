package com.example.seeme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeme.data.AppDatabase
import com.example.seeme.data.ClickCounterRepository
import com.example.seeme.ui.ClickCounterViewModel
import com.example.seeme.ui.ClickCounterViewModelFactory
import com.example.seeme.ui.theme.SeeMeTheme

class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { ClickCounterRepository(database.clickCounterDao()) }
    private val viewModel: ClickCounterViewModel by viewModels {
        ClickCounterViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeeMeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ClickCounterScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ClickCounterScreen(viewModel: ClickCounterViewModel, modifier: Modifier = Modifier) {
    val clickCount by viewModel.clickCounter.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Click Counter",
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "$clickCount",
            fontSize = 72.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { viewModel.onClickButtonPressed() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Click Me!", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.resetClicks() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Reset", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClickCounterScreenPreview() {
    SeeMeTheme {
        // This is a placeholder preview
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Click Counter",
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "0",
                fontSize = 72.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {}) {
                Text("Click Me!", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {}) {
                Text("Reset", fontSize = 16.sp)
            }
        }
    }
}
