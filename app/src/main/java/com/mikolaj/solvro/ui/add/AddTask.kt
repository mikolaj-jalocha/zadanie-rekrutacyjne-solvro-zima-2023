package com.mikolaj.solvro.ui.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.mikolaj.solvro.R
import com.mikolaj.solvro.data.network.task.Estimation
import com.mikolaj.solvro.data.network.task.Specialization

@Composable
fun AddTask(navigateUp: () -> Unit) {


    val viewModel: AddTaskViewModel = hiltViewModel()

    val uiState by viewModel.uiState

    Column {
        TextInput(
            value = uiState.taskName,
            label = stringResource(id = R.string.task_name),
            isError = viewModel.isError && uiState.taskName.isEmpty(),
            onValueChange = viewModel::updateTaskName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextInput(
            value = uiState.assignedToUser,
            label = stringResource(id = R.string.task_performer),
            onValueChange = viewModel::updateAssignedTo,
            isError = viewModel.isError && uiState.assignedToUser.isEmpty(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        DropDownMenu(
            title = stringResource(id = R.string.task_estimate),
            isError = viewModel.isError && uiState.taskEstimation.isEmpty(),
            values = Estimation.values().map { it.name },
            onValueChange = viewModel::updateEstimation,
            modifier = Modifier.padding(16.dp)
        )
        DropDownMenu(
            title = stringResource(id = R.string.task_specialization),
            isError = viewModel.isError && uiState.taskSpecialization.isEmpty(),
            values = Specialization.values().map { it.name },
            onValueChange = viewModel::updateTaskSpecialization,
            modifier = Modifier.padding(16.dp)
        )

        FilledTonalButton(
            onClick = {
                if (viewModel.addTask())
                    navigateUp()
            },
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.add_task))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Next
    ),
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        isError = isError,
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    title: String,
    values: List<String>,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
    currentValue: String = "",
) {

    var mExpanded by remember { mutableStateOf(false) }

    var mSelectedText by remember { mutableStateOf(currentValue) }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(modifier = modifier) {

        OutlinedTextField(
            isError = isError,
            value = mSelectedText,
            onValueChange = {
                mSelectedText = it
            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text(title) },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded })
            })
        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            values.forEach { label ->
                DropdownMenuItem(onClick = {
                    mSelectedText = label
                    onValueChange(label)
                    mExpanded = false
                }, text = {
                    Text(text = label)
                })
            }
        }
    }
}

