package com.example.secapp.tasks

import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secapp.R
import com.example.secapp.database.Tasks
import com.example.secapp.database.TasksEntity
import kotlinx.android.synthetic.main.fragment_list_tasks.*

/**
 * A simple [Fragment] subclass.
 */
class ListTasksFragment : Fragment() {
    private lateinit var listTaskViewModel: ListTaskViewModel
    private lateinit var adapter: TaskAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listTaskViewModel = ViewModelProvider(this).get(ListTaskViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_list_tasks, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        listTaskViewModel.getAllTasks()

        registerListener()
        registerObservers()

        startContentReceiver()
    }

    private fun registerObservers() {
        listTaskViewModel.allTasksEntity?.observe(viewLifecycleOwner, Observer { task ->
            task_progress_bar.visibility = View.INVISIBLE
            if (task != null) {
                if (task.isNotEmpty()) {
                    adapter.setItem(task)
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
            findNavController().navigate(ListTasksFragmentDirections.navigateToFirm(it.taskName))
        }
    }

    /*private fun startTaskService(tasks: List<TasksEntity>) {
        val listTasks: MutableList<Tasks> = ArrayList()
        tasks.forEach {
            val taskModel = Tasks(it.taskName, it.description, it.startDate, it.startTime, it.colorEvent, it.colorEventInt)
            listTasks.add(taskModel)
        }

        Intent(requireContext(), TaskService::class.java).also { intent ->
            intent.putParcelableArrayListExtra("Tasks", listTasks as ArrayList<out Parcelable?>?)
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TaskService.LocalBinder
            taskService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }*/

    private fun startContentReceiver(){
        val projection = arrayOf(
            COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPCION, COLUMN_COLOR_EVENT,
            COLUMN_COLOR_EVENT_INT, COLUMN_START_DATE, COLUMN_START_TIME, COLUMN_STATUS)


        val contentResolver: ContentResolver? = activity?.contentResolver
        val cursor: Cursor? = contentResolver?.query(
            URI,
            projection,
            null,
            null,
            null
        )
        if (cursor != null && cursor.count > 0) {
            Log.d("Provider", "Ok")
            val stringBuilderQueryResult = StringBuilder("")
            while (cursor.moveToNext()) {
                Log.d("Provider", "while")

                stringBuilderQueryResult.append(
                    cursor.getString(0)
                        .toString() + " , " + cursor.getString(1) + " , " + cursor.getString(2) + "\n"
                )
            }
            //textViewQueryResult.setText(stringBuilderQueryResult.toString())
        } else {
            Log.d("Provider", "else")
            //textViewQueryResult.setText("No Contacts in device")
        }
    }


    companion object{
        val URI = Uri.parse("content://com.example.firstapp.provider/TasksEntity")
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NAME = "taskName"
        const val COLUMN_DESCRIPCION = "description"
        const val COLUMN_START_DATE = "startDate"
        const val COLUMN_START_TIME = "startTime"
        const val COLUMN_COLOR_EVENT = "colorEvent"
        const val COLUMN_COLOR_EVENT_INT = "colorEventInt"
        const val COLUMN_STATUS = "status"

    }

}
