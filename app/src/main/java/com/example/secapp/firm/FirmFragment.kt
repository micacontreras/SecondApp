package com.example.secapp.firm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.secapp.R
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.fragment_firm.*


/**
 * A simple [Fragment] subclass.
 */
class FirmFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_firm, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firmListener()
    }

    private fun firmListener() {
        signature_pad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                Toast.makeText(requireContext(), "Signing", Toast.LENGTH_LONG).show()
            }

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
        }
    }


}
