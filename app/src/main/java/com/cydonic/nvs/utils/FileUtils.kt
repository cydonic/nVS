package com.cydonic.nvs.com.cydonic.nvs.utils

import java.io.File

object FileUtils {
    fun createFolder(parentDirectory: File, folderName: String): Boolean {
        val newFolder = File(parentDirectory, folderName)
        if (newFolder.exists()) {
            return false // Folder already exists
        }
        return newFolder.mkdirs()
    }

    // Add other file/folder utility functions as needed
}