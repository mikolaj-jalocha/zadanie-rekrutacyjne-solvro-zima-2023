package com.mikolaj.solvro.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolaj.solvro.data.network.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject
import kotlin.math.abs


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: TaskRepository) :
    ViewModel() {

    private val _uiState: MutableState<HomeScreenUiState> =
        mutableStateOf(HomeScreenUiState.Loading)
    val uiState: State<HomeScreenUiState> = _uiState


    fun isTaskOutdated(start: Double, end: Double): Boolean {

        val start = Date(start.toLong()).toInstant().atZone(ZoneId.systemDefault())
            .toLocalDate()
        val end = Date(end.toLong()).toInstant().atZone(ZoneId.systemDefault())
            .toLocalDate()

        return abs(start.dayOfYear - end.dayOfYear) >= 14
    }

    fun getTasks() {
        viewModelScope.launch {
            _uiState.value = HomeScreenUiState.Loading
            delay(2000L)
            val response = repository.getTasks()
            _uiState.value = if (response.isSuccessful) {
                HomeScreenUiState.Success(response.body() ?: emptyList())
            } else {
                HomeScreenUiState.Error(
                    response.message() ?: response.code().toString()
                )
            }
        }
    }
}

