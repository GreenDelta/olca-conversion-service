package app

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

import com.google.gson.Gson
import org.openlca.core.database.IDatabase
import org.openlca.core.database.derby.DerbyDatabase

class Config {

    var port: Int = 0
    var workspace: String
    var ui: String? = null
    var derbyDB: DerbyConfig? = null

    class DerbyConfig {
        var folder: String? = null
    }

    fun initDB(): IDatabase {
        if (derbyDB != null && derbyDB!!.folder != null)
            return DerbyDatabase(File(derbyDB!!.folder!!))
        throw RuntimeException("no database configuration found")
    }

    companion object {

        val default: Config
            get() {
                val config = Config()
                config.port = 8080
                config.workspace = "workspace"
                config.derbyDB = DerbyConfig()
                config.derbyDB!!.folder = "database"
                return config
            }

        fun fromFile(file: File): Config {
            val gson = Gson()
            try {
                FileInputStream(file).use { `is` -> InputStreamReader(`is`, "utf-8").use { reader -> BufferedReader(reader).use { buffer -> return gson.fromJson(buffer, Config::class.java) } } }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }
    }
}
