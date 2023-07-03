package test.kotlin.ElectermSync

import main.kotlin.ElectermSync.Config
import main.kotlin.ElectermSync.FileStore
import main.kotlin.ElectermSync.ReadResult
import main.kotlin.ElectermSync.WriteResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class FileStoreTest {
    private val dotenv = Config()
    private val userId = "testUserId"
    private lateinit var testFilePath: Path

    @BeforeEach
    fun setup() {
        testFilePath = FileStore.getFilePath(userId, dotenv)
    }

    @AfterEach
    fun tearDown() {
        try {
            Files.deleteIfExists(testFilePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun testWriteAndRead() {
        val jsonBody = "{\"key\": \"value\"}"

        val writeResult = FileStore.write(jsonBody, userId, dotenv)
        assertEquals(200, writeResult.statusCode)
        assertEquals("ok", writeResult.message)

        val readResult = FileStore.read(userId, dotenv)
        assertEquals(200, readResult.statusCode)
        assertEquals(jsonBody, readResult.fileData)
    }

    @Test
    fun testInvalidRead() {
        val readResult = FileStore.read("$userId xx", dotenv)
        assertEquals(404, readResult.statusCode)
        assertEquals("File not found", readResult.fileData)
    }
}
