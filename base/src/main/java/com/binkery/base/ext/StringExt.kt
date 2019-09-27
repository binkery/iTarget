package com.binkery.base.ext

import android.content.Context
import android.widget.Toast

/**
 * Create by binkery@gmail.com
 * on 2019 09 26
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */


fun String.longToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}