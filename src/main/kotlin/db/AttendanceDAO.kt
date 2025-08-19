package db

import model.Attendance
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.BeanMapper

class AttendanceDAO(private val jdbi: Jdbi) {

    init {
        jdbi.registerRowMapper(BeanMapper.factory(Attendance::class.java))
    }

    fun insert(att: Attendance): Int {
        return jdbi.withHandle<Int, Exception> { handle ->
            handle.createUpdate(
                """
                INSERT INTO attendance (emp_id, check_in, check_out)
                VALUES (:empId, :checkIn, :checkOut)
                """
            )
                .bindBean(att)
                .executeAndReturnGeneratedKeys("attendance_id")
                .mapTo(Int::class.java)
                .one()
        }
    }

    fun findByEmployee(empId: Int): List<Attendance> {
        return jdbi.withHandle<List<Attendance>, Exception> { handle ->
            handle.createQuery("SELECT * FROM attendance WHERE emp_id = :empId")
                .bind("empId", empId)
                .mapTo(Attendance::class.java)
                .list()
        }
    }

    fun findByDate(date: String): List<Attendance> {
        return jdbi.withHandle<List<Attendance>, Exception> { handle ->
            handle.createQuery("SELECT * FROM attendance WHERE DATE(check_in) = :date")
                .bind("date", date)
                .mapTo(Attendance::class.java)
                .list()
        }
    }

    fun findByManager(manager: String): List<Attendance> {
        return jdbi.withHandle<List<Attendance>, Exception> { handle ->
            handle.createQuery(
                """
                SELECT a.* 
                FROM attendance a
                JOIN employees e ON a.emp_id = e.emp_id
                WHERE e.reporting_to = :manager
                """
            )
                .bind("manager", manager)
                .mapTo(Attendance::class.java)
                .list()
        }
    }

    // Optional: calculate working hours for an attendance record
    fun getWorkingHours(empId: Int): List<Pair<Int, Double>> {
        return jdbi.withHandle<List<Pair<Int, Double>>, Exception> { handle ->
            handle.createQuery(
                """
                SELECT attendance_id, EXTRACT(EPOCH FROM (check_out - check_in))/3600 AS hours
                FROM attendance
                WHERE emp_id = :empId AND check_out IS NOT NULL
                """
            )
                .bind("empId", empId)
                .map { rs, _ -> rs.getInt("attendance_id") to rs.getDouble("hours") }
                .list()
        }
    }
}
