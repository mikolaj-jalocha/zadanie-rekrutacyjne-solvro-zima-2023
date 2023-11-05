package com.mikolaj.solvro.ui.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mikolaj.solvro.R
import com.mikolaj.solvro.data.network.task.Estimation
import com.mikolaj.solvro.data.network.task.Specialization
import com.mikolaj.solvro.data.network.task.TaskState
import com.mikolaj.solvro.ui.add.DropDownMenu
import com.mikolaj.solvro.ui.add.TextInput

@Composable
fun EditScreen(navigateUp: () -> Unit) {

    val viewModel: EditTaskViewModel = hiltViewModel()

    val uiState by viewModel.uiState

    if (uiState.error.isNotEmpty())
        Text(text = uiState.error)
    else {
        Column {

            TextInput(
                value = uiState.taskName,
                label = stringResource(id = R.string.task_name),
                onValueChange = viewModel::updateTaskName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            TextInput(
                value = uiState.assignedToUser,
                label = stringResource(id = R.string.task_performer),
                onValueChange = viewModel::updateAssignedTo,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            if (uiState.taskEstimation.isNotEmpty())

                DropDownMenu(
                    title = stringResource(id = R.string.task_estimate),
                    values = Estimation.values().map { it.name },
                    onValueChange = viewModel::updateEstimation,
                    currentValue = uiState.taskEstimation,
                    modifier = Modifier.padding(16.dp)
                )

            if (uiState.taskSpecialization.isNotEmpty())
                DropDownMenu(
                    title = stringResource(id = R.string.task_specialization),
                    values = Specialization.values().map { it.name },
                    onValueChange = viewModel::updateTaskSpecialization,
                    currentValue = uiState.taskSpecialization,
                    modifier = Modifier.padding(16.dp)
                )

            if (viewModel.getPossibleTaskStates().isNotEmpty() && uiState.taskState.isNotEmpty()) {

                DropDownMenu(
                    title = stringResource(id = R.string.task_state),
                    values = viewModel.getPossibleTaskStates().map { it.name },
                    onValueChange = viewModel::updateTaskState,
                    currentValue = uiState.taskState,
                    modifier = Modifier.padding(16.dp)
                )
            }

            FilledTonalButton(
                onClick = {
                    viewModel.editTask()
                    navigateUp()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text(text = stringResource(id = R.string.edit_task))
            }

            if (viewModel.originalTaskState.value != TaskState.CLOSED.name && viewModel.originalTaskState.value != TaskState.DELETED.name)
                FilledTonalButton(
                    onClick = {
                        viewModel.updateTaskState(TaskState.DELETED.name)
                        viewModel.editTask()
                        navigateUp()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                        disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)

                ) {
                    Text(text = stringResource(id = R.string.delete_task))
                }

        }
    }
}