package com.binkery.itarget.ui.counttarget

import com.binkery.itarget.ui.target.BaseAddItemFragment
import com.binkery.itarget.ui.target.TargetViewFactory

/**
 *
 *
 */
class CountTargetFactory : TargetViewFactory() {

    override fun getAddItemFragment(): BaseAddItemFragment {
        return AddItemFragment()
    }


}

