package com.mikolaj.solvro.data.network.task

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.google.gson.annotations.SerializedName
import com.mikolaj.solvro.R

enum class TaskState {
    NOT_ASSIGNED,
    IN_PROGRESS,
    CLOSED,
    DELETED,
    ALL,
}

fun TaskState.getTaskStateName(): String {
    return when (this) {
        TaskState.NOT_ASSIGNED -> "Not assigned"
        TaskState.IN_PROGRESS -> "In progress"
        TaskState.CLOSED -> "Closed"
        TaskState.DELETED -> "Deleted"
        TaskState.ALL -> "All"
    }


}
