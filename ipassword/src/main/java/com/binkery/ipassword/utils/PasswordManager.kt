package com.binkery.ipassword.utils

import android.app.Activity
import com.binkery.ipassword.PasswordCheckingActivity

class PasswordManager{

    companion object {

        fun checkPassword(activity: Activity){
            val has = SharedUtils.hasPassword(activity)
//            if(has && !SharedUtils.isToken(activity)){
//                PasswordCheckingActivity.start(activity)
//            }


        }
    }
}