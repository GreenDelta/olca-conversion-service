package app.model

import app.Server
import org.openlca.core.model.Process
import org.openlca.io.ecospold1.output.EcoSpold1Export
import org.openlca.io.ecospold1.output.ExportConfig
import java.io.BufferedOutputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ExportEcoSpold1: Export {

    override val format = Format.ECOSPOLD_1

    private val workspace = Server.workspace!!

    override fun doIt(p: Process): File {
        val file = workspace.file(p.refId, format)
        if (file.exists())
            return file
        val dir = workspace.tempDir()
        val exp = EcoSpold1Export(dir, ExportConfig.getDefault())
        exp.export(p)
        exp.close()
        packIt(dir, file)
        return file
    }

    private fun packIt(dir: File, file: File) {
        val tmp = workspace.tempFile()
        val stream = ZipOutputStream(BufferedOutputStream(tmp.outputStream()))
        val files = mutableListOf<File>()
        collectFiles(dir, files)
        stream.use { stream ->
            files.forEach { file ->
                val entry = ZipEntry(file.name)
                stream.putNextEntry(entry)
                file.inputStream().use { it.copyTo(stream) }
            }
        }
        tmp.copyTo(file, overwrite = true)
        tmp.delete()
        dir.deleteRecursively()
    }

    private fun collectFiles(dir: File, files: MutableList<File>) {
        dir.listFiles().forEach { f ->
            if (f.isDirectory) {
                collectFiles(f, files)
            } else {
                files.add(f)
            }
        }
    }
}