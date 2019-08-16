package com.binkery.itarget.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.binkery.itarget.R
import com.binkery.itarget.adapter.BaseAdapter
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.ui.TargetType
import com.binkery.itarget.utils.Utils
import kotlinx.android.synthetic.main.activity_add_target_type.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 12
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class AddTargetTypeActivity : BaseActivity() {

    private var mTargetName: String? = null

    override fun getContentLayoutId(): Int {
        return R.layout.activity_add_target_type
    }

    override fun onContentCreate(savedInstanceState: Bundle?) {

        setTitle("新增目标 - 选择打卡类型")

        mTargetName = intent.getStringExtra("target_name")

        vRecyclerView.layoutManager = LinearLayoutManager(this)
        vRecyclerView.adapter = TargetTypeAdapter(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == 101 && resultCode == Activity.RESULT_OK -> {
                finish()
            }

        }
    }


    class TargetTypeAdapter(activity: AddTargetTypeActivity) : BaseAdapter<TargetType>(activity) {

        private val types = arrayListOf<TargetType>(TargetType.MANY_COUNT, TargetType.MANY_TIME)


        override fun getItem(position: Int): TargetType? = types[position]

        override fun getItemCount(): Int = types.size

        override fun getItemViewCardType(position: Int): Int = 0

        override fun onCreateViewCard(parent: ViewGroup?, viewType: Int): BaseViewCard<TargetType> {
            return TargetTypeViewHolder()
        }

    }

    class TargetTypeViewHolder : BaseViewCard<TargetType>() {

        var vTargetTypeName: TextView? = null
        var vTargetTypeTitle: TextView? = null

        override fun onCreateView(view: View) {
            vTargetTypeName = view.findViewById(R.id.vTargetTypeName)
            vTargetTypeTitle = view.findViewById(R.id.vTargetTypeTitle)
        }

        override fun getLayoutId(): Int = R.layout.layout_add_target_type_card

        override fun onBindView(entity: TargetType?, view: View) {
            vTargetTypeName?.text = entity?.name
            vTargetTypeTitle?.text = entity?.title

        }

        override fun onItemClick(entity: TargetType?, position: Int) {

            val intent = Intent()
            when (entity) {
                TargetType.MANY_COUNT -> {
                    intent.setClass(getActivity(), AddTargetManyCountActivity::class.java)
                }
                TargetType.MANY_TIME -> {
                    intent.setClass(getActivity(), AddTargetManyTimeActivity::class.java)
                }
                else -> {
                    Utils.toast(getContext()!!, "Unsupported.")
                    return
                }

            }
            getActivity()?.startActivityForResult(intent, 101)
        }

    }

}