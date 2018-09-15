package com.illuzor.lesson.wallpapers.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.illuzor.lesson.wallpapers.R

class ProgressDialog : DialogFragment() {

    lateinit var title: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val container = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)
        container.findViewById<TextView>(R.id.tv_title).text = title

        return AlertDialog.Builder(activity)
                .setView(container)
                .create()
    }

}
