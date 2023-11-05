package com.mikolaj.solvro.ui.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolaj.solvro.data.network.TaskRepository
import com.mikolaj.solvro.data.network.task.AssignedTo
import com.mikolaj.solvro.data.network.task.Credentials
import com.mikolaj.solvro.data.network.task.Estimation
import com.mikolaj.solvro.data.network.task.Specialization
import com.mikolaj.solvro.data.network.task.TaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AddEditTaskFormsState(
    val taskName: String = "",
    val assignedToUser: String = "",
    val taskEstimation: String = "",
    val taskSpecialization: String = "",
    val taskState: String = "",
    val error: String = ""
)

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val repository: TaskRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: String

    init {
        taskId = savedStateHandle["taskId"] ?: ""
        getTask(taskId = taskId)
    }

    private val _uiState: MutableState<AddEditTaskFormsState> =
        mutableStateOf(AddEditTaskFormsState())
    val uiState: State<AddEditTaskFormsState> = _uiState

    private val _originalTaskState = mutableStateOf("")
    val originalTaskState: State<String> = _originalTaskState


    private fun getTask(taskId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = repository.getTask(taskId)
            if (task.isSuccessful) {
                task.body().let { item ->
                    _uiState.value = AddEditTaskFormsState(
                        taskName = item?.credentials?.name ?: "",
                        assignedToUser = item?.credentials?.assignedTo?.userId ?: "",
                        taskEstimation = item?.credentials?.estimation?.name ?: "",
                        taskSpecialization = item?.credentials?.specialization?.name ?: "",
                        taskState = item?.state?.name ?: ""
                    )
                    _originalTaskState.value = _uiState.value.taskState
                }
            } else {
                _uiState.value = AddEditTaskFormsState(error = task.message())
            }
        }
    }

    fun updateTaskName(taskName: String) {
        _uiState.value = _uiState.value.copy(taskName = taskName)
    }

    fun updateAssignedTo(assignedTo: String) {
        _uiState.value = _uiState.value.copy(assignedToUser = assignedTo)
    }

    fun updateEstimation(estimation: String) {
        _uiState.value = _uiState.value.copy(taskEstimation = estimation)
    }

    fun updateTaskSpecialization(specialization: String) {
        _uiState.value = _uiState.value.copy(taskSpecialization = specialization)
    }

    fun updateTaskState(taskState: String) {
        _uiState.value = _uiState.value.copy(taskState = taskState)
    }

    fun getPossibleTaskStates(): List<TaskState> {
        return if (_originalTaskState.value.isEmpty()) {
            listOf(TaskState.IN_PROGRESS, TaskState.NOT_ASSIGNED)
        } else
            when (TaskState.valueOf(_originalTaskState.value)) {
                TaskState.DELETED -> emptyList()
                TaskState.CLOSED -> emptyList()
                TaskState.NOT_ASSIGNED -> listOf(TaskState.IN_PROGRESS, TaskState.DELETED)
                TaskState.IN_PROGRESS -> listOf(
                    TaskState.IN_PROGRESS,
                    TaskState.DELETED,
                    TaskState.NOT_ASSIGNED,
                    TaskState.CLOSED
                )

                TaskState.ALL -> emptyList()
            }
    }

    fun editTask() {
        viewModelScope.launch {
            uiState.value.apply {
                repository.editTask(
                    taskId,
                    Credentials(
                        name = taskName,
                        assignedTo = AssignedTo(assignedToUser),
                        specialization = Specialization.valueOf(taskSpecialization),
                        estimation = Estimation.valueOf(taskEstimation),
                    ),
                )
                if (originalTaskState.value != uiState.value.taskState)
                    repository.editState(taskId, TaskState.valueOf(taskState))
            }
            _originalTaskState.value = _uiState.value.taskState
        }
    }
}