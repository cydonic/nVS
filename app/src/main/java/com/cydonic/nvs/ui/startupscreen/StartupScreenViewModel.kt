package com.cydonic.nvs.ui.startupscreen

import androidx.activity.result.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cydonic.nvs.com.cydonic.nvs.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class StartupScreenViewModel(private val nvsLocalDirectory: File) : ViewModel() {

    private val _folders = MutableStateFlow<List<File>>(emptyList())
    val folders: StateFlow<List<File>> = _folders.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _showDeleteFolderDialog = mutableStateOf<File?>(null)
    val showDeleteFolderDialog: State<File?> = _showDeleteFolderDialog


    init {
        loadFolders()
    }

    private fun loadFolders() {
        viewModelScope.launch {
            val folders = getFoldersFromDirectory(nvsLocalDirectory)
            _folders.value = folders
        }
    }

    private fun getFoldersFromDirectory(directory: File): List<File> {
        return directory.listFiles { file -> file.isDirectory }?.toList() ?: emptyList()
    }

    fun createFolder(folderName: String) {

        // TODO: Implement folder creation logic
        val success = FileUtils.createFolder(nvsLocalDirectory, folderName)
        if (success) {
            // Folder created successfully, update UI state
            _snackbarMessage.value = "Folder created: $folderName"
            _errorState.value = null
            loadFolders()
        } else {
            // Handle folder creation error
            _snackbarMessage.value = "Error creating folder: $folderName"
            _errorState.value = "Failed to create folder"
        }
    }

    fun onDeleteFolderDialogConfirm(folder: File) {
        viewModelScope.launch {
            if (folder.deleteRecursively()) {
                loadFolders() // Reload folders after deletion
                _snackbarMessage.value = "Folder '${folder.name}' deleted successfully"
            } else {
                _snackbarMessage.value = "Error deleting folder '${folder.name}'"
            }
        }
        _showDeleteFolderDialog.value = null // Hide the dialog
    }

    fun onDeleteFolderDialogDismiss() {
        _showDeleteFolderDialog.value = null // Hide the dialog
    }

    fun showDeleteFolderDialog(folder: File) {
        _showDeleteFolderDialog.value = folder // Show the dialog with the selected folder
    }

    fun resetSnackbarMessage() {
        _snackbarMessage.value = null
    }

}


class StartupScreenViewModelFactory(private val nvsLocalDirectory: File) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartupScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StartupScreenViewModel(nvsLocalDirectory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}