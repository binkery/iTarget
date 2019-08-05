package com.binkery.itarget.ui

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.binkery.itarget.R
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.target.TargetType
import kotlinx.android.synthetic.main.activity_add_target.*
import java.util.*

/**
 *
 *
 */
class AddTargetActivity : AppCompatActivity() {

    var targetType: TargetType = TargetType.COUNTER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_target)



        vType.text = targetType.title
        vType.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val titles = TargetType.titles()
                val index = titles.indexOf(targetType.title)
                val dialog = AlertDialog.Builder(this@AddTargetActivity)
                        .setTitle("select").setSingleChoiceItems(titles, index, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        targetType = TargetType.find(titles[which])
                        vType.text = targetType.title
                        dialog?.dismiss()
                    }

                }).create()
                dialog.show()

            }

        })

        btnSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var name = etName.editableText.toString()

                val entity = TargetEntity()
                entity.name = name
                entity.type = targetType.type
                entity.uuid = UUID.randomUUID().toString()
                DBHelper.getInstance().targetDao().insertTarget(entity)
                finish()
            }

        })

    }

}