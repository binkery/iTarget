package com.binkery.ipassword

import com.binkery.base.activity.BaseActivity

abstract class BasePasswordActivity : BaseActivity() {

    open fun shouldCheckPassword(): Boolean {
        return true
    }


}