package com.mikolaj.solvro.data.network

import com.mikolaj.solvro.data.network.task.Credentials
import com.mikolaj.solvro.data.network.task.Task

import com.mikolaj.solvro.util.Constants

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {

    @GET("/project/${Constants.PROJECT_ID}/task")
    suspend fun getTasks(): Response<List<Task>>

    @GET("/project/${Constants.PROJECT_ID}/task/{id}")
    suspend fun getTask(@Path("id") taskId: String): Response<Task>

    @POST("/project/${Constants.PROJECT_ID}/task")
    suspend fun addTask(
        @Body task: Credentials
    ): Response<String>

    @PUT("/project/${Constants.PROJECT_ID}/task/{id}")
    suspend fun updateTask(
        @Path("id") taskId: String,
        @Body task: Credentials
    ) : Response<Task>

    @PUT("/project/${Constants.PROJECT_ID}/task/{taskId}/state/")
    suspend fun updateState(
        @Path("taskId") taskId: String,
        @Body state: TaskRepositoryImpl.TaskStateBody
    ) : Response<Task>

}