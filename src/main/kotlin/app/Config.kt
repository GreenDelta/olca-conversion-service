package app

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

import com.google.gson.Gson
import org.openlca.core.database.IDatabase
import org.openlca.core.database.derby.DerbyDatabase
import org.openlca.io.maps.Maps
import org.slf4j.LoggerFactory

class Config {

    var host = "localhost"
    var port: Int = 8080
    var workspace = "workspace"
    var ui: String? = null
    var derbyDB: DerbyConfig? = null
    var mappings: String? = null

    @Transient
    private var log = LoggerFactory.getLogger(javaClass)

    class DerbyConfig {
        var folder: String? = null
    }

    fun initDB(): IDatabase {
        log.info("initialize database")
        var db: IDatabase? = null
        if (derbyDB != null && derbyDB!!.folder != null)
            db = DerbyDatabase(File(derbyDB!!.folder!!))
        if (db == null)
            throw RuntimeException("no database configuration found")
        importMappings(db)
        return db
    }

    private fun importMappings (db: IDatabase) {
        if (mappings == null)
            return
        val dir = File(mappings)
        if (!dir.exists() || !dir.isDirectory)
            return
        dir.listFiles().forEach { f ->
            if (!f.isFile)
                return@forEach
            val name = f.name
            log.info("import mapping file {}", name)
            try {
                f.inputStream().use { stream ->
                    Maps.store(f.absolutePath, stream, db)
                }
            } catch (e: Exception) {
                log.error("failed to read mapping file $f", e)
            }
        }
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
