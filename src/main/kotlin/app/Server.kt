package app

import app.model.Cache
import java.io.File
import java.net.URI

import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.grizzly.http.server.StaticHttpHandler
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.openlca.util.Strings
import org.slf4j.LoggerFactory

object Server {

    private val log = LoggerFactory.getLogger(Server::class.java)

    var refSystems = mutableListOf<RefSystem>()
    var defaultRefSystem: RefSystem? = null
    var cache: Cache? = null

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val config = getConfig(args)
            initWorkspace(config.workspace)
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

    fun getRefSystem(name: String): RefSystem {
        refSystems.forEach { rs ->
            if (Strings.nullOrEqual(name, rs.name))
                return rs
        }
        return defaultRefSystem!!
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

    private fun initWorkspace(path: String) {
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val refDir = File(dir, "refsystems")
        if (!refDir.exists()) {
            refDir.mkdirs()
        }
        refDir.listFiles().forEach { f ->
            val rs =RefSystem.initialize(f)
            refSystems.add(rs)
            if (Strings.nullOrEqual(rs.name, "default")) {
                defaultRefSystem = rs
            }
        }
        if (defaultRefSystem == null) {
            val defRefDir = File(refDir, "default")
            defaultRefSystem = RefSystem.initialize(defRefDir)
            refSystems.add(defaultRefSystem!!)
        }
        val cacheDir = File(dir, "cache")
        cache = Cache(cacheDir)
    }

    private fun addShutdownHook(server: HttpServer) {
        Runtime.getRuntime().addShutdownHook(Thread({
            try {
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
                "http://${config.host}:${config.port}/api"), resourceConfig)
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
