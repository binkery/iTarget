package com.binkery.itarget.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.binkery.itarget.R
import com.binkery.itarget.adapter.BaseAdapter
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.router.Router
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.target.FactoryManager
import kotlinx.android.synthetic.main.activity_record.*
import kotlinx.android.synthetic.main.base_activity.*

/**
 *
 *
 */
class RecordActivity : BaseActivity() {

    var mAdapter: RecordAdapter? = null
    var targetEntity: TargetEntity? = null
    var mTargetId: Int? = null


    override fun getContentLayoutId(): Int = R.layout.activity_record

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mTargetId = intent.getIntExtra("target_id", -1)
        if (mTargetId == -1) {
            finish()
            return
        }
        targetEntity = DBHelper.getInstance().targetDao().queryTargetById(mTargetId!!)
        if (targetEntity == null) {
            finish()
            return
        }

        vActionBarTitle.text = "打卡记录"
        vActionBarBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        mAdapter = RecordAdapter(this, targetEntity!!)
        vRecyclerView.layoutManager = LinearLayoutManager(this)
        vRecyclerView.adapter = mAdapter
    }


    override fun onResume() {
        super.onResume()
        val list = DBHelper.getInstance().itemDao().queryItemByTargetId(targetEntity?.id!!)
        mAdapter?.setData(list)
    }

}

class RecordAdapter(activity: RecordActivity, private val targetEntity: TargetEntity) : BaseAdapter<ItemEntity>(activity) {
    override fun getItemViewCardType(position: Int): Int {
        return targetEntity.type
    }

    override fun onCreateViewCard(parent: ViewGroup?, viewType: Int): BaseViewCard<ItemEntity> {
        return RecordViewCard(targetEntity)
    }

}

class RecordViewCard(private val targetEntity: TargetEntity) : BaseViewCard<ItemEntity>() {
    var vStartTime: TextView? = null
    var vEndTime: TextView? = null
    var vValue: TextView? = null

    override fun onCreateView(view: View) {
        vStartTime = view.findViewById(R.id.vStartTime)
        vEndTime = view.findViewById(R.id.vEndTime)
        vValue = view.findViewById(R.id.vValue)
    }

    override fun getLayoutId(): Int = R.layout.layout_record_card_count

    override fun onBindView(entity: ItemEntity, view: View) {
        FactoryManager.getFactory(targetEntity.type).updateRecordView(vStartTime!!, vEndTime!!, vValue!!, entity)
    }

    override fun onItemClick(entity: ItemEntity?, position: Int) {
        Router.startAddItemActivity(getActivity()!!, entity!!.id)
    }

}