package com.example.secapp.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.secapp.database.TaskDataBase
import com.example.secapp.database.TasksEntity
import com.example.secapp.database.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TasksRepository

    var allTasksEntity: LiveData<List<TasksEntity>>? = null

    init {
        val tasksDao = TaskDataBase.getDatabase(application).taskDao()
        repository = TasksRepository(tasksDao)
    }

    fun getAllTasks() {
        allTasksEntity = repository.allTasksEntity
    }

    fun insert(task: TasksEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }

}