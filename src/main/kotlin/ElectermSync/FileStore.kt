package main.kotlin.ElectermSync

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

object FileStore {
    @JvmStatic
    fun main(args: Array<String>) {
        // Test code
    }

    fun getFilePath(userId: String, dotenv: Config): Path {
        val storePath = dotenv.getValue("FILE_STORE_PATH")
        val folder: Path = storePath.let { Path.of(it) } ?: Path.of(System.getProperty("user.dir"))
        return folder.resolve("$userId.json")
    }

    fun write(jsonBody: String, userId: String, dotenv: Config): WriteResult {
        val filePath = getFilePath(userId, dotenv)
        return try {
            Files.writeString(filePath, jsonBody)
            WriteResult("ok", 200)
        } catch (e: IOException) {
            WriteResult("Error writing file", 500)
        }
    }

    fun read(userId: String, dotenv: Config): ReadResult {
        val filePath = getFilePath(userId, dotenv)
        val file = filePath.toFile()
        return if (file.isFile) {
            try {
                val fileContent = Files.readString(filePath).toString()
                ReadResult(fileContent, 200)
            } catch (e: IOException) {
                ReadResult("File read error", 500)
            }
        } else {
            ReadResult("File not found", 404)
        }
    }
}
