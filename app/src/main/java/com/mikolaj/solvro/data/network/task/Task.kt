package com.mikolaj.solvro.data.network.task


data class Task(
    val _id: String ="",
    val createdAt: Double = 0.0,
    val createdBy: CreatedBy = CreatedBy(),
    val credentials: Credentials= Credentials(),
    val dateRange: DateRange = DateRange(),
    val projectId: String ="",
    val state: TaskState = TaskState.NOT_ASSIGNED
)