package com.mikolaj.solvro.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mikolaj.solvro.R
import com.mikolaj.solvro.data.network.task.Estimation
import com.mikolaj.solvro.data.network.task.Task
import com.mikolaj.solvro.data.network.task.TaskState
import com.mikolaj.solvro.data.network.task.getTaskStateName
import com.mikolaj.solvro.data.network.task.toNumber


sealed interface HomeScreenUiState {
    data class Success(val tasks: List<Task>) : HomeScreenUiState
    object Loading : HomeScreenUiState
    class Error(val message: String) : HomeScreenUiState
}

@Composable
fun HomeScreen(
    navigateToEditTask: (taskId: String) -> Unit
) {
    val viewModel: HomeScreenViewModel = hiltViewModel()

    val screenState = remember {
        mutableStateOf(TaskState.NOT_ASSIGNED)
    }
    val state = viewModel.uiState.value

    LaunchedEffect(Unit) {
        viewModel.getTasks()
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TaskState.values().forEach {
                FilledTonalButton(
                    onClick = { screenState.value = it },
                    enabled = screenState.value != it,
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        disabledContainerColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = it.getTaskStateName(), style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        when (state) {
            is HomeScreenUiState.Success -> {
                SuccessScreen(
                    tasks = state.tasks.filter { if (screenState.value == TaskState.ALL) true else it.state == screenState.value },
                    navigateToEditTask = navigateToEditTask,
                    isTaskOutDated = viewModel::isTaskOutdated
                )
            }

            is HomeScreenUiState.Loading -> {
                LoadingScreen()
            }

            is HomeScreenUiState.Error -> {
                ErrorScreen(text = state.message)
            }
        }
    }
}


@Composable
fun ErrorScreen(text: String, modifier: Modifier = Modifier) {
    Text(text = stringResource(R.string.error) + text)
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LinearProgressIndicator()
    }
}

@Composable
fun SuccessScreen(
    tasks: List<Task>,
    navigateToEditTask: (taskId: String) -> Unit,
    isTaskOutDated: (Double, Double) -> Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tasks) {
            TodoTaskItem(
                title = it.credentials.name,
                estimation = it.credentials.estimation,
                onTaskClick = {
                    navigateToEditTask(it._id)
                },
                modifier = Modifier.padding(4.dp),
                isOutdated = isTaskOutDated(it.dateRange.start, it.dateRange.end)
            )
        }
    }
}


@Composable
fun TodoTaskItem(
    title: String,
    estimation: Estimation,
    onTaskClick: () -> Unit,
    modifier: Modifier = Modifier,
    isOutdated: Boolean = false
) {
    OutlinedButton(
        onClick = onTaskClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isOutdated) MaterialTheme.colorScheme.errorContainer else Color.Transparent,
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(2f)
        )
        Text(
            text = stringResource(id = R.string.task_estimate) + " " + estimation.toNumber().toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }


}

