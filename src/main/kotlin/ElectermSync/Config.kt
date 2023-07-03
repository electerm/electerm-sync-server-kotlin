package main.kotlin.ElectermSync

import io.github.cdimascio.dotenv.Dotenv

class Config {
    private val dotenv: Dotenv = Dotenv.load()

    fun getValue(name: String): String {
        return dotenv.get(name) ?: ""
    }
}
