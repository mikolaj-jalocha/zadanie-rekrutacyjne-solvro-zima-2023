package com.mikolaj.solvro.ui.add

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolaj.solvro.data.network.TaskRepository
import com.mikolaj.solvro.data.network.task.AssignedTo
import com.mikolaj.solvro.data.network.task.Credentials
import com.mikolaj.solvro.data.network.task.Estimation
import com.mikolaj.solvro.data.network.task.Specialization
import com.mikolaj.solvro.ui.edit.AddEditTaskFormsState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddTaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {


    private val _uiState: MutableState<AddEditTaskFormsState> = mutableStateOf(AddEditTaskFormsState())
    val uiState: State<AddEditTaskFormsState> = _uiState

    var isError by mutableStateOf(false)
        private set

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


    private fun checkData(): Boolean {
        uiState.value.apply {
            isError =
                !(taskName.isNotEmpty() && taskEstimation.isNotEmpty() && taskSpecialization.isNotEmpty() && assignedToUser.isNotEmpty())
        }
        return isError
    }

    fun addTask(): Boolean {
        if (checkData())
            return false
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value.apply {
                val taskCredentials = Credentials(
                    name = taskName,
                    estimation = Estimation.valueOf(taskEstimation),
                    specialization = Specialization.valueOf(taskSpecialization),
                    assignedTo = AssignedTo(
                        assignedToUser
                    )
                )
                repository.addTask(taskCredentials)
            }
        }
        return true
    }
}