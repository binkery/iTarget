package com.binkery.itarget.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import com.binkery.itarget.R
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 13
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class Dialogs {

    companion object {


        fun showEditTextDialog(activity: Activity, text: String, title: String, listener: OnTextChangedListener) {

            val view = LayoutInflater.from(activity).inflate(R.layout.dialog_edittext, null)
            val vOk = view.findViewById<TextView>(R.id.vOk)
            val vCancel = view.findViewById<TextView>(R.id.vCancel)
            val vEditText = view.findViewById<EditText>(R.id.vEditText)
            vEditText.setText(text)

            val dialog = AlertDialog.Builder(activity).setTitle(title).setView(view).create()
            vOk.setOnClickListener({
                listener.onTextChanged(vEditText.text.toString())
                dialog.dismiss()
            })
            vCancel.setOnClickListener({
                dialog.dismiss()
            })
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()

        }

        fun showSingleChoiceItems(activity: Activity, items: Array<String>, index: Int, listener: OnSingleChoiceItemSelectedListener) {

            val dialog = AlertDialog.Builder(activity).setTitle("").setSingleChoiceItems(items, index, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                    listener.onSelected(which, items[which])
                }
            }).create()
            dialog.show()
        }

        fun showTimePicker(context: Context, hour: Int, minute: Int, listener: OnDateTimeChangedListener) {

            var currentHour = hour
            var currentMinute = minute

            val view = LayoutInflater.from(context).inflate(R.layout.dialog_time_picker, null)
            val vTimePicker = view.findViewById<TimePicker>(R.id.vTimePicker)
            val vOk = view.findViewById<TextView>(R.id.vOk)
            val vCancel = view.findViewById<TextView>(R.id.vCancel)
            vTimePicker.setIs24HourView(true)
            vTimePicker.currentHour = currentHour
            vTimePicker.currentMinute = currentMinute
            val dialog = AlertDialog.Builder(context).setTitle("").setView(view).create()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            vTimePicker.setOnTimeChangedListener(TimePicker.OnTimeChangedListener { view, hourOfDay, minute ->
                currentHour = hourOfDay
                currentMinute = minute
            })
            vCancel.setOnClickListener({
                dialog.dismiss()
            })
            vOk.setOnClickListener({
                listener.onChanged(0, 0, 0, currentHour, currentMinute)
                dialog.dismiss()
            })
        }


        fun showTimePicker(context: Context, baseDate: Long, baseTime: Long, listener: OnMilliSecondsChangeListener) {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
            calendar.timeInMillis = baseTime
            var currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            var currentMinute = calendar.get(Calendar.MINUTE)
            calendar.timeInMillis = baseDate
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_time_picker, null)
            val vTimePicker = view.findViewById<TimePicker>(R.id.vTimePicker)
            val vOk = view.findViewById<TextView>(R.id.vOk)
            val vCancel = view.findViewById<TextView>(R.id.vCancel)
            vTimePicker.setIs24HourView(true)
            vTimePicker.currentHour = currentHour
            vTimePicker.currentMinute = currentMinute
            val dialog = AlertDialog.Builder(context).setTitle("").setView(view).create()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            vTimePicker.setOnTimeChangedListener(TimePicker.OnTimeChangedListener { view, hourOfDay, minute ->
                currentHour = hourOfDay
                currentMinute = minute
            })
            vCancel.setOnClickListener({
                dialog.dismiss()
            })
            vOk.setOnClickListener({
                calendar.set(Calendar.YEAR, currentYear)
                calendar.set(Calendar.MONTH, currentMonth)
                calendar.set(Calendar.DAY_OF_MONTH, currentDay)
                calendar.set(Calendar.HOUR_OF_DAY, currentHour)
                calendar.set(Calendar.MINUTE, currentMinute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                listener.onChanged(calendar.timeInMillis)
                dialog.dismiss()
            })
        }

    }
}