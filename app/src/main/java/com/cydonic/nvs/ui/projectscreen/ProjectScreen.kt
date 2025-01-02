package com.cydonic.nvs.ui.projectscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cydonic.nvs.R
import com.cydonic.nvs.com.cydonic.nvs.ui.projectscreen.ProjectScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    folderName: String,
    onNavigateBack: () -> Unit, // Callback to navigate back to StartupScreen
    viewModel: ProjectScreenViewModel // Inject ViewModel
) {

    val folderTree = viewModel.folderTree.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "nVS",
                            modifier = Modifier.clickable { onNavigateBack() }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = folderName)
                        Spacer(Modifier.weight(1f)) // Push icon to the end
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_round),
                            contentDescription = "App Icon",
                            modifier = Modifier.size(48.dp) // Adjust size as needed
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(folderTree) { node ->
                Text(text = node.name) // Basic tree representation
            }
        }

    }
}