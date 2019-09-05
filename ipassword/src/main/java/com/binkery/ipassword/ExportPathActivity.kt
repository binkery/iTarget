package com.binkery.ipassword

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import com.binkery.base.utils.Utils
import com.binkery.ipassword.code.CodeEntity
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity
import com.binkery.ipassword.utils.PasswordDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_export_path.*
import java.io.File

class ExportPathActivity : BasePasswordActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, ExportPathActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private var mPassword = ""
    private var mPath = ""

    override fun getContentLayoutId(): Int = R.layout.activity_export_path

    override fun onContentCreate(savedInstanceState: Bundle?) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        vPassword.text = mPassword
        vModifyPassword.setOnClickListener {
            PasswordDialog.show(this, "设置文件密码", mPassword, object : PasswordDialog.OnComfirmListener {
                override fun onComfirm(password: String) {
                    mPassword = password
                    vPassword.text = mPassword
                }

            })
        }

        vNextStep.setOnClickListener {
            if (mPassword.length < 4) {
                Utils.toast(this, "请设置四位数密码")
                return@setOnClickListener
            }
            val datas = DBHelper.instance.itemDao().queryAll()
            val json = Gson().toJson(datas)

//            val file = File(mPath)
//            file.writeText(json, Charsets.UTF_8)

            CodeEntity.encode2File(mPath, json, mPassword)

            val decodeJson = CodeEntity.decodeFromFile(mPath, mPassword)
            val dataList = Gson().fromJson<List<ItemEntity>>(decodeJson, object : TypeToken<List<ItemEntity>>() {}.type)
            for (d in dataList) {
                Utils.log("item = " + d.id + "," + d.name + "," + d.username + "," + d.password + "," + d.comments)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mPath = getFilePath(this, "ipassword")
            vPath.text = mPath
        }
    }

    private fun getFilePath(context: Context, dir: String): String {
        val directoryPath: String =
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
                    Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dir
                } else {//没外部存储就使用内部存储
                    context.getFilesDir().toString() + File.separator + dir
                }
        val file = File(directoryPath)
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs()
        }
        val fileName = Utils.datetimeFormat("yyyy-MM-dd-HH-mm-ss", System.currentTimeMillis()) + ".pwd"
        val exportPath = File(file, fileName)
        return exportPath.absolutePath
    }
}