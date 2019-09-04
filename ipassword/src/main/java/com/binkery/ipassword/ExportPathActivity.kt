package com.binkery.ipassword

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_export_path.*
import java.io.File

class ExportPathActivity : BasePasswordActivity() {

    companion object {
        fun start(activity: Activity,password:String) {
            val intent = Intent(activity, ExportPathActivity::class.java)
            intent.putExtra("password",password)
            activity.startActivity(intent)
        }
    }

    private var mPassword = ""

    override fun getContentLayoutId(): Int = R.layout.activity_export_path

    override fun onContentCreate(savedInstanceState: Bundle?) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        mPassword = intent.getStringExtra("password")
        vPassword.text = mPassword
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val path = getFilePath(this, "ipassword")
            vPath.text = path
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
        return file.absolutePath
    }
}