package app

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

import com.google.gson.Gson

class Config {

    var host = "localhost"
    var port: Int = 8080
    var workspace = "workspace"
    var ui: String? = null

    companion object {

        val default: Config
            get() {
                val config = Config()
                config.port = 8080
                config.workspace = "workspace"
                return config
            }

        fun fromFile(file: File): Config {
            val gson = Gson()
            FileInputStream(file).use { stream ->
                InputStreamReader(stream, "utf-8").use { reader ->
                    BufferedReader(reader).use { buffer ->
                        return gson.fromJson(buffer, Config::class.java)
                    }
                }
            }
        }
    }
}
