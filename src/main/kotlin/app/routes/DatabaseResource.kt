package app.routes

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status

import com.google.gson.Gson
import app.Server
import org.openlca.core.database.ActorDao
import org.openlca.core.database.FlowDao
import org.openlca.core.database.FlowPropertyDao
import org.openlca.core.database.ProcessDao
import org.openlca.core.database.RootEntityDao
import org.openlca.core.database.SourceDao
import org.openlca.core.database.UnitGroupDao

@Path("database")
class DatabaseResource {

    val actors: Response
        @GET
        @Path("actors")
        @Produces(MediaType.APPLICATION_JSON)
        get() = getDescriptors(ActorDao(Server.db))

    val sources: Response
        @GET
        @Path("sources")
        @Produces(MediaType.APPLICATION_JSON)
        get() = getDescriptors(SourceDao(Server.db))

    val unitGroups: Response
        @GET
        @Path("unitGroups")
        @Produces(MediaType.APPLICATION_JSON)
        get() = getDescriptors(UnitGroupDao(Server.db))

    val flowProperties: Response
        @GET
        @Path("flowProperties")
        @Produces(MediaType.APPLICATION_JSON)
        get() = getDescriptors(FlowPropertyDao(Server.db))

    val flows: Response
        @GET
        @Path("flows")
        @Produces(MediaType.APPLICATION_JSON)
        get() = getDescriptors(FlowDao(Server.db))

    val processes: Response
        @GET
        @Path("processes")
        @Produces(MediaType.APPLICATION_JSON)
        get() = getDescriptors(ProcessDao(Server.db))

    private fun getDescriptors(dao: RootEntityDao<*, *>): Response {
        try {
            val descriptors = dao.descriptors
            val gson = Gson()
            val json = gson.toJson(descriptors)
            return Response.ok(json).build()
        } catch (e: Exception) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(e).build()
        }

    }

}
