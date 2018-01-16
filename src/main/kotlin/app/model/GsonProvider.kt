package app.routes

import com.google.gson.Gson
import java.io.*
import java.lang.reflect.Type
import javax.ws.rs.Consumes
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyReader
import javax.ws.rs.ext.MessageBodyWriter
import javax.ws.rs.ext.Provider

/**
 * Use Gson for object de-/serialization.
 * see https://stackoverflow.com/questions/9516224/using-gson-instead-of-jackson-in-jersey
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON, "text/json")
@Produces(MediaType.APPLICATION_JSON, "text/json")
class GsonProvider : MessageBodyReader<Any>, MessageBodyWriter<Any>{

    override fun isReadable(type: Class<*>?, genericType: Type?,
                            annotations: Array<out Annotation>?,
                            mediaType: MediaType?): Boolean {
        return true
    }

    override fun readFrom(type: Class<Any>?, genericType: Type?,
                          annotations: Array<out Annotation>?,
                          mediaType: MediaType?,
                          httpHeaders: MultivaluedMap<String, String>?,
                          stream: InputStream?): Any {
        BufferedReader(InputStreamReader(stream, "utf-8")).use {
            return Gson().fromJson(it, type)
        }
    }

    override fun isWriteable(type: Class<*>?, genericType: Type?,
                             annotations: Array<out Annotation>?,
                             mediaType: MediaType?): Boolean {
        return true
    }

    override fun writeTo(t: Any?, type: Class<*>?, genericType: Type?,
                         annotations: Array<out Annotation>?,
                         mediaType: MediaType?,
                         httpHeaders: MultivaluedMap<String, Any>?,
                         stream: OutputStream?) {
        BufferedWriter(OutputStreamWriter(stream, "utf-8")).use {
            Gson().toJson(t, it)
        }
    }

}