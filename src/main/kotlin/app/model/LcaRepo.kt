package app.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.openlca.core.model.ModelType
import org.openlca.jsonld.EntityStore
import org.openlca.jsonld.Schema
import org.openlca.jsonld.output.Context
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.net.URL

/**
 * A helper class for retrieving public files from
 */
class LcaRepo(private val url: String) : EntityStore {

    override fun close() {
    }

    override fun contains(type: ModelType, id: String): Boolean {
        // TODO: very inefficient
        val data = get(type, id)
        return data != null
    }

    override fun get(type: ModelType, id: String): JsonObject? {
        return try {
            val bytes = get(type.name + "/" + id)
            val reader = InputStreamReader(
                    ByteArrayInputStream(bytes),
                    "utf-8")
            Gson().fromJson(reader, JsonObject::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override fun get(path: String): ByteArray {
        URL(url + "/" + path).openStream().use { stream ->
            return stream.readBytes()
        }
    }

    override fun getBinFiles(type: ModelType, id: String?): List<String> {
        return emptyList()
    }

    override fun getContext(): JsonObject {
        return Context.write(Schema.URI)
    }

    override fun getRefIds(type: ModelType): List<String> {
        return emptyList()
    }

    override fun put(type: ModelType, obj: JsonObject) {
    }

    override fun put(path: String, data: ByteArray) {
    }

    override fun putBin(type: ModelType, id: String, file: String, data: ByteArray) {
    }

    override fun putContext() {
    }
}
