package com.example.secapp.tasks

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secapp.R
import com.example.secapp.database.Converters
import com.example.secapp.database.TasksEntity
import com.example.secapp.statusProvider
import kotlinx.android.synthetic.main.fragment_list_tasks.*

/**
 * A simple [Fragment] subclass.
 */
class ListTasksFragment : Fragment() {
    private lateinit var listTaskViewModel: ListTaskViewModel
    private lateinit var adapter: TaskAdapter

    private var listTasks: MutableList<TasksEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listTaskViewModel = ViewModelProvider(this).get(ListTaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_list_tasks, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        listTaskViewModel.getAllTasks()

        registerListener()
        registerObservers()

    }

    private fun registerObservers() {
        listTaskViewModel.allTasksEntity?.observe(viewLifecycleOwner, Observer { task ->
            task_progress_bar.visibility = View.INVISIBLE
            if (task != null) {
                if (task.isNotEmpty()) {
                    adapter.setItem(task)
                    listTasks.addAll(task)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Empty List",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                task_recycler_view.visibility = View.GONE
            }
            if (statusProvider) startContentReceiver() else updateTask()
        })
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(requireContext())
        task_recycler_view.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }

    private fun registerListener() {
        adapter.onClick = {
            if (it.status == getString(R.string.complete)) {
                Toast.makeText(requireContext(), "Task complete", Toast.LENGTH_LONG).show()
            } else {
                findNavController().navigate(ListTasksFragmentDirections.navigateToFirm(it.taskName))
            }
        }
    }

    private fun startContentReceiver() {
        statusProvider = false
        val projection = arrayOf(
            COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPCION, COLUMN_COLOR_EVENT,
            COLUMN_COLOR_EVENT_INT, COLUMN_START_DATE, COLUMN_START_TIME, COLUMN_STATUS, COLUMN_FIRM
        )

        val contentResolver: ContentResolver? = activity?.contentResolver
        val cursor: Cursor? = contentResolver?.query(
            URI,
            projection,
            null,
            null,
            null
        )
        if (cursor != null && cursor.count > 0) {
            var index = 0
            while (cursor.moveToNext()) {
                if (cursor.getString(7) == COLUMN_IN_PROGRESS) {
                    if (listTasks.size != 0) {
                        if (cursor.getLong(0) != listTasks[index].id) {
                            insertTask(cursor)
                        }
                    } else {
                        insertTask(cursor)
                    }
                }
                index += 1
            }
        } else {
            Log.d("Provider", "else")
        }
    }


    private fun updateTask() {
        val updateValues = ContentValues().apply {
            put("firm", getString(R.string.firm))
        }
        val selectionClause: String = getString(R.string.complete)

        val selectionArgs: Array<String> = arrayOf(getString(R.string.inProgress))

        val rowsUpdated = activity?.contentResolver?.update(
            URI,
            updateValues,
            selectionClause,
            selectionArgs
        )!!

        Log.d("update", rowsUpdated.toString())
    }

    private fun insertTask(cursor: Cursor) {
        listTaskViewModel.insert(
            TasksEntity(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                Converters().fromTimestamp(cursor.getLong(3))!!,
                Converters().fromTimestamp(cursor.getLong(4))!!,
                cursor.getString(5),
                cursor.getInt(6),
                cursor.getString(7),
                cursor.getString(8)
            )
        )
    }


    companion object {
        val URI = Uri.parse("content://com.example.firstapp.provider/TasksEntity")
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NAME = "taskName"
        const val COLUMN_DESCRIPCION = "description"
        const val COLUMN_START_DATE = "startDate"
        const val COLUMN_START_TIME = "startTime"
        const val COLUMN_COLOR_EVENT = "colorEvent"
        const val COLUMN_COLOR_EVENT_INT = "colorEventInt"
        const val COLUMN_STATUS = "status"
        const val COLUMN_IN_PROGRESS = "In Progress"
        const val COLUMN_FIRM = "firm"
    }

}
