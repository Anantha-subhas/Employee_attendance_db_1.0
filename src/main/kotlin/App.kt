import db.AttendanceDAO
import db.EmployeeDAO
import io.dropwizard.Application
import io.dropwizard.jdbi3.JdbiFactory
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.jdbi.v3.core.Jdbi
import resource.EmployeeResource
import com.fasterxml.jackson.module.kotlin.KotlinModule

class App : Application<AppConfig>() {

    override fun initialize(bootstrap: Bootstrap<AppConfig>) {
        // No special initialization
    }

    override fun run(config: AppConfig, env: Environment) {
        // Register Kotlin module for Jackson
        env.objectMapper.registerModule(KotlinModule())

        // Create Jdbi instance
        val factory = JdbiFactory()
        val jdbi: Jdbi = factory.build(env, config.database, "postgresql")

        // Register DAO
        val employeeDao = EmployeeDAO(jdbi)
        val attendanceDao = AttendanceDAO(jdbi)
        // Register REST resources
//        env.jersey().register(EmployeeResource(employeeDao))
        env.jersey().register(EmployeeResource(employeeDao, attendanceDao))

    }
}

fun main(args: Array<String>) {
    App().run(*args)
}
