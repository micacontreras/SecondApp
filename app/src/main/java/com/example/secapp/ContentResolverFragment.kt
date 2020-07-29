package com.example.secapp

import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


/**
 * A simple [Fragment] subclass.
 */
class ContentResolverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(activity).apply {
            setText(R.string.hello_blank_fragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentResolver: ContentResolver = getContentResolver()
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            mColumnProjection,
            null,
            null,
            null
        )
        if (cursor != null && cursor.getCount() > 0) {
            val stringBuilderQueryResult = StringBuilder("")
            while (cursor.moveToNext()) {
                stringBuilderQueryResult.append(
                    cursor.getString(0)
                        .toString() + " , " + cursor.getString(1) + " , " + cursor.getString(2) + "\n"
                )
            }
            textViewQueryResult.setText(stringBuilderQueryResult.toString())
        } else {
            textViewQueryResult.setText("No Contacts in device")
        }
    }

}
