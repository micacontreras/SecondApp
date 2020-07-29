package com.example.secapp.database

import androidx.lifecycle.LiveData

class TasksRepository(private val tasksDao: TasksDao) {

    val allTasksEntity: LiveData<List<TasksEntity>> = tasksDao.getAll()

    suspend fun insert(task: TasksEntity) {
        tasksDao.insert(task)
    }

    fun getTask(taskName: String): LiveData<TasksEntity> {
        return tasksDao.getTask(taskName)
    }
}