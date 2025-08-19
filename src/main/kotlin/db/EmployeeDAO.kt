package db

import model.Employee
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.BeanMapper

class EmployeeDAO(private val jdbi: Jdbi) {

    init {
        // Register BeanMapper once for Employee
        jdbi.registerRowMapper(BeanMapper.factory(Employee::class.java))
    }

    fun insert(emp: Employee): Int {
        return jdbi.withHandle<Int, Exception> { handle ->
            handle.createUpdate(
                """
                INSERT INTO employees (first_name, last_name, role, department, reporting_to)
                VALUES (:firstName, :lastName, :role, :department, :reportingTo)
                """
            )
//                .bind("firstName", emp.firstName)
//                .bind("lastName", emp.lastName)
//                .bind("role", emp.role)
//                .bind("department", emp.department)
//                .bind("reportingTo", emp.reportingTo)
                .bindBean(emp)
                .executeAndReturnGeneratedKeys("emp_id")
                .mapTo(Int::class.java)
                .one()
        }
    }

    fun findById(id: Int): Employee? {
        return jdbi.withHandle<Employee?, Exception> { handle ->
            handle.createQuery(
                """
                SELECT 
                    emp_id AS "empId",
                    first_name AS "firstName",
                    last_name AS "lastName",
                    role,
                    department,
                    reporting_to AS "reportingTo"
                FROM employees
                WHERE emp_id = :id
                """
            )
                .bind("id", id)
                .mapTo(Employee::class.java)
                .findOne()
                .orElse(null)
        }
    }

    fun listAll(): List<Employee> {
        return jdbi.withHandle<List<Employee>, Exception> { handle ->
            handle.createQuery(
                """   
                SELECT 
                    emp_id AS "empId",
                    first_name AS "firstName",
                    last_name AS "lastName",
                    role,
                    department,
                    reporting_to AS "reportingTo"
                FROM employees
                """
            )
                .mapTo(Employee::class.java)
                .list()
        }
    }

    fun delete(id: Int): Boolean {
        return jdbi.withHandle<Int, Exception> { handle ->
            handle.createUpdate("DELETE FROM employees WHERE emp_id = :id")
                .bind("id", id)
                .execute()
        } > 0
    }
//   1 > 0 so it is true → at least one employee with that emp_id was deleted
}
