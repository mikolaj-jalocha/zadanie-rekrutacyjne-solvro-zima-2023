package com.mikolaj.solvro.data.network.task
data class Credentials(
    val name: String = "",
    val assignedTo: AssignedTo = AssignedTo(),
    val estimation: Estimation = Estimation.ONE,
    val specialization: Specialization = Specialization.BACKEND
)
