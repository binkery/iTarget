package com.binkery.itarget.ui

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.binkery.itarget.R
import com.binkery.itarget.adapter.BaseAdapter
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.router.Router
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.target.FactoryManager
import com.binkery.itarget.ui.target.TargetType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.base_activity.*

class MainActivity : BaseActivity() {

    var mAdapter: TargetAdapter? = null

    override fun getContentLayoutId(): Int = R.layout.activity_main


    override fun onContentCreate(savedInstanceState: Bundle?) {
        vAddTarget.setOnClickListener(
                object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        Router.startAddTargetActivity(this@MainActivity)
                    }
                }
        )

        vActionBarTitle.text = "iTarget"
        vActionBarBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        mAdapter = TargetAdapter(this)
        targetRecyclerView.layoutManager = LinearLayoutManager(this)
        val divider = ItemDecoration()
        targetRecyclerView.addItemDecoration(divider)
        targetRecyclerView.adapter = mAdapter
    }

    override fun onResume() {
        super.onResume()
        val list: MutableList<TargetEntity>? = DBHelper.getInstance().targetDao().queryAllTarget()
        mAdapter?.setData(list)
    }
}

class TargetAdapter(activity: MainActivity) : BaseAdapter<TargetEntity>(activity) {

    override fun getItemViewCardType(position: Int): Int {
        return 0
    }

    override fun onCreateViewCard(parent: ViewGroup?, viewType: Int): BaseViewCard<TargetEntity> {
        return HomeViewCard()
    }

}

class ItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect?.set(0, 0, 0, 10)
    }

}

class HomeViewCard : BaseViewCard<TargetEntity>() {

    private var vTargetName: TextView? = null
    private var vTargetStatus: TextView? = null
    private var vTargetCount: TextView? = null
    private var vTargetType: TextView? = null

    override fun onCreateView(view: View) {
        vTargetName = view.findViewById(R.id.vTargetName)
        vTargetStatus = view.findViewById(R.id.vTargetStatus)
        vTargetCount = view.findViewById(R.id.vTargetCount)
        vTargetType = view.findViewById(R.id.vTargetType)
    }

    override fun getLayoutId(): Int = R.layout.layout_home_card_common

    override fun onBindView(entity: TargetEntity, view: View) {
        vTargetName?.text = entity.name
        vTargetType?.text = TargetType.find(entity.type).title
        val list = DBHelper.getInstance().itemDao().queryItemByTargetId(entity.id)

        vTargetStatus?.text = FactoryManager.getFactory(entity.type).getTargetStatus(list)
        vTargetCount?.text = FactoryManager.getFactory(entity.type).getTargetSummary(list)

    }

    override fun onItemClick(entity: TargetEntity?, position: Int) {
        Router.startTargetViewActivity(getActivity()!!, entity?.id!!)
    }

}