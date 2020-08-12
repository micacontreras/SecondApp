package com.example.secapp.firm

import android.content.ContentValues
import android.os.Bundle
import android.provider.UserDictionary
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.arch.core.executor.TaskExecutor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.secapp.R
import com.example.secapp.database.TasksEntity
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.fragment_firm.*


/**
 * A simple [Fragment] subclass.
 */
class FirmFragment : Fragment() {
    private val args: FirmFragmentArgs by navArgs()

    private lateinit var taskNameSaved: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_firm, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        firmListener()
    }

    private fun loadData() {
        if (args.taskName != "null" && args.taskName != null) {
            taskNameSaved = args.taskName
        }
    }

    private fun firmListener() {
        signature_pad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {}

            override fun onSigned() {
                clear_button.isEnabled = true
                save_button.isEnabled = true
            }

            override fun onClear() {
                clear_button.isEnabled = true
                save_button.isEnabled = true
            }
        })

        clear_button.setOnClickListener{ signature_pad.clear() }

        save_button.setOnClickListener {
            Toast.makeText(requireContext(), "Signature saved", Toast.LENGTH_LONG).show()
            findNavController().navigate(FirmFragmentDirections.navigateToDetail(taskNameSaved))
        }
    }
}
