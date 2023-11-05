package com.mikolaj.solvro.data.network

import com.mikolaj.solvro.data.network.task.Credentials
import com.mikolaj.solvro.data.network.task.Task
import com.mikolaj.solvro.data.network.task.TaskState
import retrofit2.Response


interface TaskRepository {

    suspend fun getTasks(): Response<List<Task>>
    suspend fun getTask(taskId: String): Response<Task>
    suspend fun addTask(taskCredentials: Credentials): Response<String>
    suspend fun editTask(taskId: String, taskCredentials: Credentials) : Response<Task>
    suspend fun editState(taskId: String, taskState: TaskState) : Response<Task>

}