package model

//data class Employee(
//    val empId: Int = 0,
//    val firstName: String = "",
//    val lastName: String = "",
//    val role: String = "",
//    val department: String = "",
//    val reportingTo: String? = null
//)

data class Employee(

    var firstName: String = "",
    var lastName: String = "",
    var role: String = "",
    var department: String = "",
    var reportingTo: String? = null
){
    var empId: Int = 0
}

