package resource

import db.AttendanceDAO
import db.EmployeeDAO
import model.Attendance
import model.Employee
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class EmployeeResource(
    private val employeeDao: EmployeeDAO,
    private val attendanceDao: AttendanceDAO
) {

    @POST
    fun createEmployee(emp: Employee): Response {
        val id = employeeDao.insert(emp)
        val created = employeeDao.findById(id)
        return Response.status(Response.Status.CREATED).entity(created).build()
    }

    @GET
    @Path("/{id}")
    fun getEmployee(@PathParam("id") id: Int): Response {
        val emp = employeeDao.findById(id)
        return if (emp != null) Response.ok(emp).build()
        else Response.status(Response.Status.NOT_FOUND).build()
    }

    @GET
    fun listEmployees(): List<Employee> = employeeDao.listAll()

    @DELETE
    @Path("/{id}")
    fun deleteEmployee(@PathParam("id") id: Int): Response {
        return if (employeeDao.delete(id)) Response.noContent().build()
        else Response.status(Response.Status.NOT_FOUND).build()
    }

    @GET
    @Path("/{id}/with-attendance") // Jersey path patterns must be unique per HTTP method (GET, POST, etc.) to avoid ambiguity.
    fun getEmployeeWithAttendance(@PathParam("id") id: Int): Response {
        val emp = employeeDao.findById(id)
        return if (emp != null) {
            val attendanceList = attendanceDao.findByEmployee(id)
            val result = mapOf(
                "employee" to emp,
                "attendance" to attendanceList
            )
            Response.ok(result).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    //  Attendance

    @POST
    @Path("/{id}/attendance")
    fun addAttendance(@PathParam("id") empId: Int, att: Attendance): Response {
        att.empId = empId // assign that passed emp_id to this attendance's id to connect this table
        val id = attendanceDao.insert(att)
        return Response.status(Response.Status.CREATED).entity(att).build()
    }

    @GET
    @Path("/{id}/attendance")
    fun getAttendanceByEmployee(@PathParam("id") empId: Int): List<Attendance> {
        return attendanceDao.findByEmployee(empId)
    }

    @GET
    @Path("/attendance/date/{date}")
    fun getAttendanceByDate(@PathParam("date") date: String): List<Attendance> {
        return attendanceDao.findByDate(date)
    }

    @GET
    @Path("/attendance/manager/{manager}")
    fun getAttendanceByManager(@PathParam("manager") manager: String): List<Attendance> {
        return attendanceDao.findByManager(manager)
    }

    @GET
    @Path("/{id}/attendance/hours")
    fun getWorkingHours(@PathParam("id") empId: Int): List<Map<String, Any>> {
        return attendanceDao.getWorkingHours(empId).map { (attId, hours) ->
            mapOf("attendanceId" to attId, "workingHours" to hours)
        }
    }
}
