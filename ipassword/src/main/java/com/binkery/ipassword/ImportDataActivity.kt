package com.binkery.ipassword

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.view.View
import androidx.core.app.ActivityCompat
import com.binkery.base.activity.BaseActivity
import com.binkery.base.utils.Dialogs
import com.binkery.base.utils.Utils
import com.binkery.ipassword.code.CodeEntity
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity
import com.binkery.ipassword.utils.SharedUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_import_data.*
import java.io.File

/**
 * Create by binkery@gmail.com
 * on 2019 09 06
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class ImportDataActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, ImportDataActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_import_data

    override fun onContentCreate(savedInstanceState: Bundle?) {
        vAppbar.setTitle("导入数据")
        vPathChooser.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                fileChooser()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            }

        }
        vPath.text = ""

        vAppbar.setRightItem("开始导入",-1,View.OnClickListener {

            val path = vPath.text.toString()
            val password = vPasswordInput.text.toString()
            if (path.isEmpty()) {
                Utils.toast(this, "文件路径为空")
                return@OnClickListener
            }
            if (password.length < 4) {
                Utils.toast(this, "密码长度必需为 4 位")
                return@OnClickListener
            }
            val decodeJson = CodeEntity.decodeFromFile(path, password)
            Utils.log("decode json = " + decodeJson)
            if (decodeJson == null) {
                Utils.toast(this, "密码错误")
                return@OnClickListener
            }
            val dataList = Gson().fromJson<List<ItemEntity>>(decodeJson, object : TypeToken<List<ItemEntity>>() {}.type)
            for (d in dataList) {
                Utils.log("item = " + d.id + "," + d.name + "," + d.username + "," + d.password + "," + d.comments)
                val itemEntity = ItemEntity()
                itemEntity.comments = d.comments
                itemEntity.name = d.name
                itemEntity.password = d.password
                itemEntity.username = d.username
                DBHelper.instance.itemDao().insert(itemEntity)
            }
            Dialogs.alert(this, "导入成功", "导入" + dataList.size + "条数据。", "朕知道了", View.OnClickListener {
                finish()
            })
        })
    }

    private fun fileChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        SharedUtils.updateToken(this)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data



            Utils.log("uri = " + uri.toString())
            Utils.log("uri authority " + uri?.authority)
            val id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                DocumentsContract.getDocumentId(uri)
            } else {
                TODO("VERSION.SDK_INT < KITKAT")
            }
            Utils.log("id = " + id)
            val spit = id.split(":")
            if ("primary" == spit[0].toLowerCase()) {
                val path = Environment.getExternalStorageDirectory().absolutePath + File.separator + spit[1]
                vPath.text = path
            }
//            if(uri?.authority == ExternalStorageStats.CONTENTS_FILE_DESCRIPTOR)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fileChooser()
        }
    }

}