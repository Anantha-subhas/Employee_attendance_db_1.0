package model

import java.time.LocalDateTime

data class Attendance(
    var empId: Int = 0,
    var checkIn: LocalDateTime = LocalDateTime.now(),
    var checkOut: LocalDateTime? = null
) {
    var attendanceId: Int = 0
}
// attendanceId matches the primary key attendance_id.
