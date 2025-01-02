package com.cydonic.nvs.com.cydonic.nvs.ui.projectscreen

import androidx.activity.result.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class ProjectScreenViewModel(private val folder: File) : ViewModel() {
    private val _folderTree = mutableStateOf<List<TreeNode>>(emptyList())
    val folderTree: State<List<TreeNode>> = _folderTree

    init {
        loadFolderTree()
    }

    private fun loadFolderTree() {
        viewModelScope.launch {
            _folderTree.value = buildTree(folder)
        }
    }

    private fun buildTree(file: File): List<TreeNode> {
        val nodes = mutableListOf<TreeNode>()
        if (file.isDirectory) {
            nodes.add(TreeNode(file.name, file.absolutePath, true))
            file.listFiles()?.forEach { child ->
                nodes.addAll(buildTree(child))
            }
        } else {
            nodes.add(TreeNode(file.name, file.absolutePath, false))
        }
        return nodes
    }
}

data class TreeNode(val name: String, val path: String, val isDirectory: Boolean)

class ProjectScreenViewModelFactory(private val folder: File) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectScreenViewModel(folder) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}