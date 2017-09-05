package com.victor.yunweatherkotlin.activity

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import com.victor.yunweatherkotlin.R
import com.victor.yunweatherkotlin.adapter.QuickAdapter
import com.victor.yunweatherkotlin.db.County
import kotlinx.android.synthetic.main.activity_edit_citys.*
import org.litepal.crud.DataSupport

class EditCitysActivity : BaseActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mData: MutableList<County>
    private lateinit var mAdapter: QuickAdapter
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mItemDragAndSwipeCallback: ItemDragAndSwipeCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_citys)
        initView()
        setListener()
        swipeAble()

    }

    private fun swipeAble() {
        mData = DataSupport.findAll(County::class.java)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        val onItemSwipeListener = object : OnItemSwipeListener {
            override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "view swiped start: " + pos)
                val holder = viewHolder as BaseViewHolder
            }

            override fun clearView(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "View reset: " + pos)
                val holder = viewHolder as BaseViewHolder
            }

            override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d("haha", "View Swiped: " + pos)
                mData.removeAt(pos)
//                DataSupport.deleteAll(County::class.java)
//                for (county in mData){
//                    county.save()
//                }
            }

            override fun onItemSwipeMoving(canvas: Canvas, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, isCurrentlyActive: Boolean) {
                canvas.drawColor(ContextCompat.getColor(this@EditCitysActivity, R.color.color_light_blue))
                //                canvas.drawText("Just some text", 0, 40, paint);
            }
        }
        mAdapter = QuickAdapter(mData)
        mItemDragAndSwipeCallback = ItemDragAndSwipeCallback(mAdapter)
        mItemTouchHelper = ItemTouchHelper(mItemDragAndSwipeCallback)
        mItemTouchHelper.attachToRecyclerView(mRecyclerView)
        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START or ItemTouchHelper.END)
        mAdapter.enableSwipeItem()
        mAdapter.setOnItemSwipeListener(onItemSwipeListener)
        mRecyclerView.adapter = mAdapter
    }


    private fun initView(){
        mRecyclerView = recycler
    }

    private fun setListener() {
        iv_back.setOnClickListener { finish() }
        iv_add.setOnClickListener {
            startActivity(Intent(this, AddCityActivity::class.java))
        }
    }

    companion object {
        private val TAG = "EditCityActivity"
    }
}
