package com.binkery.itarget.ui

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.router.Router
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.target.FactoryManager
import com.binkery.itarget.ui.target.TargetType
import kotlinx.android.synthetic.main.base_activity.*
import kotlinx.android.synthetic.main.fragment_target_view_count.*
import java.util.*

/**
 *
 *
 */
class TargetViewActivity : BaseActivity() {

    private var targetEntity: TargetEntity? = null
    private var mAdapter: DateAdapter? = null


    override fun getContentLayoutId(): Int = R.layout.activity_target_detail

    override fun onContentCreate(savedInstanceState: Bundle?) {
        val targetId = intent.getIntExtra("target_id", -1)
        if (targetId == -1) {
            finish()
            return
        }
        targetEntity = DBHelper.getInstance().targetDao().queryTargetById(targetId)
        if (targetEntity == null) {
            finish()
            return
        }
        vActionBarTitle.text = targetEntity?.name
        FactoryManager.getFactory(targetEntity?.type!!).updateTargetViewActivity(this, targetEntity!!)
        vActionBarBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        vTargetRecord.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startTargetRecordActivity()
            }
        })

        vRecyclerView.layoutManager = GridLayoutManager(this, 7)
        mAdapter = DateAdapter(targetEntity!!)
        vRecyclerView.adapter = mAdapter

        vTargetSetting.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Router.startSettingActivity(this@TargetViewActivity, targetEntity?.id!!)
            }

        })

        update()
    }

    fun update(){
        val list = DBHelper.getInstance().itemDao().queryItemByTargetId(targetEntity?.id!!)
        vTargetCountSum.text = FactoryManager.getFactory(targetEntity?.type!!).getTargetSummary(list)
        vTargetType.text = TargetType.find(targetEntity?.type!!).title
        mAdapter?.updateDate(list)
        vRecyclerView.scrollToPosition(Int.MAX_VALUE / 2 - (14))
    }

    fun startTargetRecordActivity() {
        Router.startTargetRecordActivity(this, targetEntity?.id!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_target_view, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_delete -> {
                DBHelper.getInstance().targetDao().deteteTarget(targetEntity)
                finish()
            }
            R.id.menu_edit -> {

            }
            R.id.menu_record -> {
                startTargetRecordActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class DateAdapter(val targetEntity: TargetEntity) : RecyclerView.Adapter<DateViewHolder>() {

        private val ONE_DAY = 1000L * 60 * 60 * 24

        private var mListItem: MutableList<ItemEntity>? = null

        fun updateDate(list:MutableList<ItemEntity>) {
            mListItem = list
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: DateViewHolder?, position: Int) {
            val calendar = Calendar.getInstance()
            val offset = calendar.get(Calendar.DAY_OF_WEEK)
            calendar.add(Calendar.DAY_OF_MONTH, position + offset - 1 - (Int.MAX_VALUE / 2))

            val ms = calendar.timeInMillis - (calendar.timeInMillis % (1000 * 60 * 60 * 24))

            val mon = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            when {
                day == 1 -> holder?.vDate?.text = (mon + 1).toString() + "/" + day.toString()
                else -> holder?.vDate?.text = day.toString()
            }

            val result = mListItem?.filter { it.startTime > ms && it.startTime < ms + ONE_DAY }
            when{
                result?.isEmpty()!! -> holder?.vCount?.text = ""
                else -> {
                    holder?.vCount?.text = FactoryManager.getFactory(targetEntity.type).updateDateView(result.toMutableList())
                }
            }

        }

        override fun getItemCount(): Int {
            return Int.MAX_VALUE
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DateViewHolder {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.layout_date_item, parent, false)
            return DateViewHolder(view)
        }

    }

    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vDate: TextView
        val vCount: TextView

        init {
            vDate = view.findViewById(R.id.vDate)
            vCount = view.findViewById(R.id.vCount)
        }
    }

}