package com.binkery.itarget.ui.target

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.sqlite.TargetEntity

/**
 *
 *
 */
abstract class BaseAddItemFragment : Fragment() {

    companion object {

        fun newInstance(target: TargetEntity, itemId: Int): BaseAddItemFragment {

            val fragment = TargetType.find(target.type).factory.getAddItemFragment()
            val args = Bundle()
            args.putInt("target_id", target.id)
            args.putInt("item_id", itemId)
            fragment.arguments = args
            return fragment
        }
    }

    final override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val targetId = arguments.getInt("target_id", TargetType.COUNTER.type)
        val itemId = arguments.getInt("item_id", -1)
        val target = DBHelper.getInstance().targetDao().queryTargetById(targetId)
        if (itemId == -1) {
            onFragmentCreate(view, target, null)
        } else {
            val itemEntity = DBHelper.getInstance().itemDao().queryItemById(itemId)
            onFragmentCreate(view, target, itemEntity)
        }
    }

    final override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(getLayoutId(), container, false)
    }

    abstract fun getLayoutId(): Int

    abstract fun onFragmentCreate(view: View?, targetEntity: TargetEntity, itemEntity: ItemEntity?)

}