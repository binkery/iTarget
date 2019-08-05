package com.binkery.itarget.ui.durationtarget

import com.binkery.itarget.ui.target.BaseAddItemFragment
import com.binkery.itarget.ui.target.TargetViewFactory

/**
 *
 *
 */
class DurationTargetFactory : TargetViewFactory() {


    override fun getAddItemFragment(): BaseAddItemFragment {
        return AddItemDurationFragment()
    }


}