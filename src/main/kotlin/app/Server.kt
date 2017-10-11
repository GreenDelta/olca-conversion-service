package app

import java.io.File
import java.net.URI

import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.grizzly.http.server.StaticHttpHandler
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.openlca.core.database.IDatabase
import org.slf4j.LoggerFactory

object Server {

    private val log = LoggerFactory.getLogger(Server::class.java)

    var db: IDatabase? = null

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val config = getConfig(args)
            db = config.initDB()
            val server = createServer(config)
            addShutdownHook(server)
            log.info("starting server")
            server.start()
            log.info("Press CTRL^C to exit..")
            Thread.currentThread().join()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getConfig(args: Array<String>?): Config {
        val file = File("config.json")
        if (file.exists())
            return Config.fromFile(file)
        return if (args != null && args.isNotEmpty())
            Config.fromFile(File(args[0]))
        else
            Config.default
    }

    private fun addShutdownHook(server: HttpServer) {
        Runtime.getRuntime().addShutdownHook(Thread({
            try {
                db!!.close()
                server.shutdownNow()
            } catch (e: Exception) {
                log.error("failed to shutdown service", e)
            }
        }, "shutdownHook"))
    }

    private fun createServer(config: Config): HttpServer {
        val resourceConfig = ResourceConfig().packages(
                "app.routes")
        val server = GrizzlyHttpServerFactory.createHttpServer(URI.create(
                "http://localhost:" + config.port + "/api"), resourceConfig)
        if (config.ui != null) {
            val uiDir = File(config.ui!!)
            if (uiDir.isDirectory) {
                val ui = StaticHttpHandler(config.ui, "/")
                server.serverConfiguration.addHttpHandler(ui)
            }
        }
        return server
    }
}
