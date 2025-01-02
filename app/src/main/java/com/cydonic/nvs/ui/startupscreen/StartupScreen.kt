package com.cydonic.nvs.ui.startupscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cydonic.nvs.R
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartupScreen(
    viewModel: StartupScreenViewModel, // Add ViewModel parameter
    navController: NavHostController,
    onCloneRepoClick: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    var folderName by remember { mutableStateOf("") }

    val showDeleteFolderDialog = viewModel.showDeleteFolderDialog.value

    val folders by viewModel.folders.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues -> Spacer(modifier = Modifier.padding(paddingValues))

    val snackbarMessage by viewModel.snackbarMessage.collectAsState()


            // Handle Snackbar message
    LaunchedEffect(snackbarMessage) {
        val message = snackbarMessage
        if (message != null) {
            SnackbarHostState().showSnackbar(message, duration = SnackbarDuration.Long)
            viewModel.resetSnackbarMessage()
        }
    }
        }

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



            // Handle error state (e.g., display an error message)
            if (errorState != null) {
                Text("Error: $errorState", color = Color.Red)
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(onClick = { showDialog = true})
            {
                Icon(
                    imageVector = Icons.Filled.CreateNewFolder,
                    contentDescription = "Create Folder"
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Create Folder")
            }

            if (showDialog) {
                FolderNameDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { name ->
                        folderName = name
                        showDialog = false
                        viewModel.createFolder(folderName)
                    }
                )
            }

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

            if (showDeleteFolderDialog != null) {
                FolderDeleteDialog(
                    folderName = showDeleteFolderDialog.name,
                    onConfirm = { viewModel.onDeleteFolderDialogConfirm(showDeleteFolderDialog) },
                    onDismiss = { viewModel.onDeleteFolderDialogDismiss() }
                )
            }

            LazyColumn {
                items(folders) { folder ->

                    Box(
                        modifier = Modifier
                            .combinedClickable(
                                onClick = { navController.navigate("projectScreen/${folder.name}") }, // Regular click action
                                onLongClick = { viewModel.showDeleteFolderDialog(folder) } // Long click action
                            )
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.shapes.medium
                            ) // Optional button-like appearance
                            .border(
                                BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                MaterialTheme.shapes.medium
                            )
                            .padding(16.dp) // Adjust padding for content
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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



@Composable
fun FolderNameDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var folderName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Folder") },
        text = {
            OutlinedTextField(
                value = folderName,
                onValueChange = { folderName = it },
                label = { Text("Folder Name") }
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(folderName) }) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun FolderDeleteDialog(
    folderName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Folder") },
        text = { Text("Are you sure you want to delete the folder '$folderName'?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


