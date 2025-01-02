package com.cydonic.nvs.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import androidx.wear.compose.foundation.weight
import com.cydonic.nvs.R
import com.cydonic.nvs.R.mipmap.ic_launcher_round
import java.io.File

class StartupScreenPlayground : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box {
                StartupScreen(
                    folders = listOf(File("Folder 1"), File("Folder 2")),
                    onOpenFolderClick = { /* Handle open folder */ },
                    onCreateFolderClick = { /* Handle create folder */ },
                    onCloneRepoClick = { /* Handle sync with GitHub */ }
                )

                }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartupScreen(
    folders: List<File>, // List of folders in nVS local directory
    onOpenFolderClick: (File) -> Unit,
    onCreateFolderClick: () -> Unit,
    onCloneRepoClick: () -> Unit
) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF2E8CF))
            )  {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            OutlinedButton(onClick = onCreateFolderClick) {
                Icon(
                    imageVector = Icons.Filled.CreateNewFolder,
                    contentDescription = "Create Folder"
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Create Folder")
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(onClick = onCloneRepoClick) {
                Icon(
                    imageVector = Icons.Filled.Download, // Or a suitable icon for cloning
                    contentDescription = "Clone Repo"
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Clone Repo")
            }

            // Display list of folders using LazyColumn or RecyclerView

            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(folders) { folder ->
                    OutlinedButton(onClick = { onOpenFolderClick(folder) }) {
                        Icon(
                            imageVector = Icons.Filled.FolderOpen,
                            contentDescription = "Open Folder"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(folder.name)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2E2E2E))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "nVS",
                color = Color(0xFFF2E8CF),
                style = MaterialTheme.typography.headlineMedium, // Adjust style as needed
                modifier = Modifier.weight(1f) // Occupy available space
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_round), // Replace with your icon resource
                contentDescription = "App Icon",
                modifier = Modifier.size(48.dp) // Adjust size as needed
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StartupScreen(
        folders = listOf(File("Folder 1"), File("Folder 2")),
        onOpenFolderClick = { /* Handle open folder */ },
        onCreateFolderClick = { /* Handle create folder */ },
        onCloneRepoClick = { /* Handle sync with GitHub */ }
    )
}