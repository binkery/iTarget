package com.binkery.itarget.ui

import android.content.Intent
import android.os.Bundle
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.ui.activity.AddTargetTypeActivity
import kotlinx.android.synthetic.main.activity_add_target.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class AddTargetActivity : BaseActivity() {

    private var mTargetType: TargetType = TargetType.MANY_COUNT
    private var mStringTargetName: String? = null

    override fun getContentLayoutId(): Int = R.layout.activity_add_target

    override fun onContentCreate(savedInstanceState: Bundle?) {

        setTitle("新增目标 - 输入目标名称")

        vNextStep.setOnClickListener({

            val intent = Intent(this@AddTargetActivity, AddTargetTypeActivity::class.java)
            intent.putExtra("target_name", vTargetName.text.toString())
            startActivity(intent)
        })

//        vActionBarBack.setOnClickListener({
//            finish()
//        })
//        vActionBarTitle.text = "新增目标"

//        vTargetName.setValue("")
//        vTargetName.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                val view = LayoutInflater.from(applicationContext).inflate(R.layout.dialog_edittext, null)
//                val vOk = view.findViewById<TextView>(R.id.vOk)
//                val vCancel = view.findViewById<TextView>(R.id.vCancel)
//                val vEditText = view.findViewById<EditText>(R.id.vEditText)
//                vEditText.setText(vTargetName.getValue())
//                val dialog = AlertDialog.Builder(this@AddTargetActivity)
//                        .setTitle("输入目标名称").setView(view).create()
//                vOk.setOnClickListener(object : View.OnClickListener {
//                    override fun onClick(v: View?) {
//                        vTargetName.setValue(vEditText.text.toString())
//                        mStringTargetName = vEditText.text.toString()
//                        dialog.dismiss()
//                    }
//                })
//                vCancel.setOnClickListener(object : View.OnClickListener {
//                    override fun onClick(v: View?) {
//                        dialog.dismiss()
//                    }
//                })
//                dialog.setCancelable(false)
//                dialog.setCanceledOnTouchOutside(false)
//                dialog.show()
//
//            }
//
//        })
//
//        vTargetType.setValue(TargetType.COUNTER.title)
//        vTargetType.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//
//                val titles = TargetType.titles()
//                val dialog = AlertDialog.Builder(this@AddTargetActivity)
//                        .setTitle("select").setSingleChoiceItems(titles, mTargetType.value, object : DialogInterface.OnClickListener {
//                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                        vTargetType.setValue(TargetType.title(which))
//                        mTargetType = TargetType.find(which)
//                        dialog?.dismiss()
//                    }
//
//                }).create()
//                dialog.show()
//
//            }
//
//        })
//
//        vTargetValue.setValue("")
//        vTargetValue.setOnClickListener({
//            when (mTargetType) {
//                TargetType.ON_TIME_EVERYDAY -> {
//                    var selectTime = System.currentTimeMillis()
//                    val dateTimePicker = DateTimePicker(this, { timestamp ->
//                        selectTime = (timestamp / Const.ONE_MINUTE) * Const.ONE_MINUTE
//                        vStartTime.setValue(TextFormater.dataTimeWithoutSecond(timestamp))
//                    }, selectTime - Const.ONE_YEAR, selectTime + Const.ONE_YEAR)
//                    dateTimePicker.show(selectTime)
//                }
//            }
//        })
//
//        vSave.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                if (TextUtils.isEmpty(mStringTargetName)) {
//                    Toast.makeText(this@AddTargetActivity, "Name is null ", Toast.LENGTH_LONG).show()
//                    return
//                }
//                val entity = TargetEntity()
//                entity.name = mStringTargetName
//                entity.type = mTargetType.value
//                entity.uuid = UUID.randomUUID().toString()
//                DBHelper.getInstance().targetDao().insertTarget(entity)
//                finish()
//            }
//
//        })

    }

}