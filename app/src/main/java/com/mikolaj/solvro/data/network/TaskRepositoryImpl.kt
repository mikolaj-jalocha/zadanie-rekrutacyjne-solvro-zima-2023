package com.mikolaj.solvro.data.network


import com.mikolaj.solvro.data.network.task.Credentials
import com.mikolaj.solvro.data.network.task.Task
import com.mikolaj.solvro.data.network.task.TaskState

import retrofit2.Response

class TaskRepositoryImpl(private val api: TaskApi) : TaskRepository {
    override suspend fun getTasks(): Response<List<Task>> {
        return api.getTasks()
    }

    override suspend fun getTask(taskId: String): Response<Task> {
        return api.getTask(taskId)
    }

    override suspend fun editTask(
        taskId: String,
        taskCredentials: Credentials,
    ): Response<Task> {
        return api.updateTask(taskId, taskCredentials)
    }

    override suspend fun editState(
        taskId: String,
        taskState: TaskState
    ): Response<Task> {
        return api.updateState(taskId, TaskStateBody(taskState.name))
    }

    override suspend fun addTask(taskCredentials: Credentials): Response<String> {
        return api.addTask(taskCredentials)
    }

    data class TaskStateBody(val state: String)
}