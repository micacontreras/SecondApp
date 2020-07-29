package com.example.secapp.tasks

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

}
