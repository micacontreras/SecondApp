package com.example.secapp.detail

import android.content.ContentValues
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.secapp.R
import com.example.secapp.database.Converters
import com.example.secapp.database.TasksEntity
import com.example.secapp.firm.DetailViewModel
import com.example.secapp.showDialog
import kotlinx.android.synthetic.main.fragment_detail_task.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DetailTask : androidx.fragment.app.Fragment() {
    private val args: DetailTaskArgs by navArgs()
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var task: TasksEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_detail_task, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()

        detail_task_complete.setOnClickListener {
            detailViewModel.insert(
                TasksEntity(
                    task.id,
                    task.taskName,
                    task.description,
                    task.startDate,
                    task.startTime,
                    task.colorEvent,
                    task.colorEventInt,
                    getString(R.string.complete),
                    getString(R.string.firm)
                )
            )
            showDialog(requireContext(),
                "Complete",
                "Navigate to FirstApp?",
                "Ok",
                {   updateTask(task)
                    startActivity(activity?.packageManager?.getLaunchIntentForPackage("com.example.firstapp")) },
                "Cancel",
                { findNavController().navigateUp() })

        }

    }

    private fun loadData() {
        if (args.taskName != "null" && args.taskName != null) {
            detailViewModel.getTask(args.taskName)
                ?.observe(viewLifecycleOwner, androidx.lifecycle.Observer { taskSearched ->
                    taskSearched.let {
                        task = taskSearched
                        setupScreen(taskSearched)
                    }
                })
        }
    }

    private fun setupScreen(task: TasksEntity) {
        detail_title.editText?.setText(task.taskName)
        detail_title_et.setText(task.taskName)
        detail_description.editText?.setText(task.description)
        detail_date_start.text = loadDate(task.startDate)[0]
        detail_hour_start.text = loadDate(task.startTime)[1]

        detail_color_image.background.setColorFilter(
            Color.parseColor(task.colorEvent),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun loadDate(dateSaved: Date? = null): List<String> {
        var date = 0L
        val dateList: MutableList<String> = ArrayList()

        date = dateSaved?.time!!
        //Otra forma fecha
        val format: DateFormat = SimpleDateFormat("E, dd MMM yyyy")
        val dateFormat = format.format(date)
        dateList.add(dateFormat)

        //Otra forma hora
        val format2: DateFormat = SimpleDateFormat("hh:mm a")
        val timeFormat = format2.format(date)
        dateList.add(timeFormat)

        return dateList
    }
    private fun updateTask(tasksEntity: TasksEntity) {
        val updateValues = ContentValues().apply {
            put("_id", tasksEntity.id)
            put("taskName", tasksEntity.taskName)
            put("description", tasksEntity.description)
            put("startDate", Converters().dateToTimestamp(tasksEntity.startDate))
            put("startTime", Converters().dateToTimestamp(tasksEntity.startTime))
            put("colorEvent", tasksEntity.colorEvent)
            put("colorEventInt", tasksEntity.colorEventInt)
            put("status", getString(R.string.complete))
            put("firm", getString(R.string.firm))
        }

        val rowsUpdated = activity?.contentResolver?.update(
            Uri.parse("content://com.example.firstapp.provider/TasksEntity/${tasksEntity.id}"),
            updateValues,
            null,
            null
        )!!

        Log.d("update", rowsUpdated.toString())
    }

}
