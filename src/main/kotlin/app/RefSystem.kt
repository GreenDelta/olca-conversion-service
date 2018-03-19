package app

import org.openlca.core.database.IDatabase
import org.openlca.core.database.derby.DerbyDatabase
import org.openlca.io.maps.Maps
import org.openlca.jsonld.ZipStore
import org.openlca.jsonld.input.JsonImport
import org.slf4j.LoggerFactory
import java.io.File

class RefSystem private constructor(
        val name: String,
        private val dumpFolder: String) {

    fun db(): IDatabase {
        return DerbyDatabase.restoreInMemory(dumpFolder)
    }

    companion object {

        private val log = LoggerFactory.getLogger(RefSystem::class.java)

        fun initialize(folder: File): RefSystem {
            log.info("Initialize reference system under {}", folder)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val dumpDir = File(folder, "dump")
            if (!dumpDir.exists()) {
                initDump(folder)
            }
            return RefSystem(folder.name, dumpDir.absolutePath)
        }

        private fun initDump(dir: File) {
            log.info("Initialize database dump")
            val memDB = DerbyDatabase.createInMemory()
            val dataFile = File(dir, "data.zip")
            if (dataFile.exists()) {
                importJson(dataFile, memDB)
            }
            val mappingDir = File(dir, "mappings")
            if (mappingDir.exists() && mappingDir.isDirectory) {
                importMappings(mappingDir, memDB)
            }
            val dumpDir = File(dir, "dump")
            memDB.dump(dumpDir.absolutePath)
            memDB.close()
        }

        private fun importJson(dataFile: File, db: IDatabase) {
            log.info("Import JSON data from {}", dataFile)
            try {
                val store = ZipStore.open(dataFile)
                val imp = JsonImport(store, db)
                imp.run()
            } catch (e: Exception) {
                log.error("Failed to import reference data from $dataFile", e)
            }
        }

        private fun importMappings(mappingDir: File, db: IDatabase) {
            log.info("Import mapping files from {}", mappingDir)
            mappingDir.listFiles().forEach { f ->
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
    }
}