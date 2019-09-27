package com.binkery.ipassword

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.core.app.ActivityCompat
import com.binkery.base.ext.gone
import com.binkery.base.ext.longToast
import com.binkery.base.ext.visible
import com.binkery.base.utils.Utils
import com.binkery.ipassword.utils.SharedUtils
import kotlinx.android.synthetic.main.activity_auto_backup.*

class AutoBackupActivity : BasePasswordActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, AutoBackupActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_auto_backup

    override fun onContentCreate(savedInstanceState: Bundle?) {

        vAppbar.setTitle("自动备份设置")
        vAppbar.setRightItem("保存", -1, View.OnClickListener {
            val open = vCheckBox.isChecked
            if (!open) {
                SharedUtils.closeAutoBackup(this)
                finish()
                return@OnClickListener
            }
            val password = vPasswordInput.text.toString()
            if (password.length < 6) {
                "密码少于 6 位数".longToast(this)
                return@OnClickListener
            }
            val hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                Utils.toast(this, "未授权 SDCard 写权限")
                return@OnClickListener
            }
            SharedUtils.openAutoBackup(this, password)
            finish()
        })

        vCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            updateWithCheckBox(isChecked)
        }
        vCheckBox.isChecked = SharedUtils.isOpenAutoBackup(this)
        updateWithCheckBox(vCheckBox.isChecked)

        vPasswordInput.setText(SharedUtils.getExportKey(this))

        val hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            vCheckPermission.text = "已授权"
        } else {
            vCheckPermission.text = "未授权，点击申请授权"
            vCheckPermission.setOnClickListener {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
            }
        }

    }

    private fun updateWithCheckBox(isChecked: Boolean) {
        if (isChecked) {
            vPasswordDescription.visible()
            vPasswordInput.visible()
            vPermissionDescription.visible()
            vCheckPermission.visible()
        } else {
            vPasswordDescription.gone()
            vPasswordInput.gone()
            vPermissionDescription.gone()
            vCheckPermission.gone()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            vCheckPermission.text = "已授权"
        } else {
            Utils.toast(this, "未授权 SDCard 的写权限，将无法开启自动备份功能")
        }
    }
}