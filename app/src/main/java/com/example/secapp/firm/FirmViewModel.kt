package com.example.secapp.firm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.secapp.database.TaskDataBase
import com.example.secapp.database.TasksEntity
import com.example.secapp.database.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirmViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TasksRepository

    init {
        val tasksDao = TaskDataBase.getDatabase(application).taskDao()
        repository = TasksRepository(tasksDao)
    }

    fun insert(task: TasksEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }

    fun getTask(taskName: String): LiveData<TasksEntity>? = repository.getTask(taskName)
}