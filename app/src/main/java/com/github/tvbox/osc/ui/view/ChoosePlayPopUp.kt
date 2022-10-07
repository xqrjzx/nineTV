package com.github.tvbox.osc.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.github.tvbox.osc.R
import com.github.tvbox.osc.base.App
import com.github.tvbox.osc.bean.VodInfo
import com.github.tvbox.osc.bean.VodInfo.VodSeries
import com.github.tvbox.osc.cache.RoomDataManger
import com.github.tvbox.osc.event.RefreshEvent
import com.github.tvbox.osc.ui.activity.PlayEvent
import com.github.tvbox.osc.ui.adapter.SeriesAdapter
import com.github.tvbox.osc.util.FastClickCheckUtil
import com.owen.tvrecyclerview.widget.TvRecyclerView
import com.owen.tvrecyclerview.widget.V7GridLayoutManager
import org.greenrobot.eventbus.EventBus
import razerdp.basepopup.BasePopupWindow

class ChoosePlayPopUp(context: Context?) : BasePopupWindow(context) {
    private var seriesAdapter: SeriesAdapter? = null
    private var mGridView: TvRecyclerView? = null

    init {
        setContentView(R.layout.choose_play_layout)
        mGridView = contentView.findViewById(R.id.mGridView)
        mGridView = findViewById(R.id.mGridView)
        seriesAdapter = SeriesAdapter()
        mGridView?.run {
            setHasFixedSize(true)
            layoutManager = V7GridLayoutManager(getContext(), 6)
            adapter = seriesAdapter
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
        val vodInfo = App.instance?.getVodInfo()

        val list: MutableList<VodSeries> =
            vodInfo?.seriesMap?.get(vodInfo.playFlag) ?: mutableListOf()


        seriesAdapter?.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                FastClickCheckUtil.check(view)
                if ((list.size) > 0) {
                    for (j in list.indices) {
                        seriesAdapter!!.data[j].selected = false
                        seriesAdapter!!.notifyItemChanged(j)
                    }
                    //解决倒叙不刷新
                    if (vodInfo?.playIndex != position) {
                        seriesAdapter!!.data[position].selected = true
                        seriesAdapter!!.notifyItemChanged(position)
                        vodInfo?.playIndex = position
                    }
                    seriesAdapter!!.data[vodInfo?.playIndex ?: 0].selected = true
                    seriesAdapter!!.notifyItemChanged(vodInfo?.playIndex ?: 0)
                    insertVod(vodInfo ?: return@OnItemClickListener)
                    EventBus.getDefault().post(PlayEvent().apply {
                        this.vodInfo = vodInfo
                    })
                }
            }
        setBackgroundColor(Color.TRANSPARENT)
        val pFont = Paint()
        val rect = Rect()
        var w = 1
        for (i in list.indices) {
            val name = list[i].name
            pFont.getTextBounds(name, 0, name.length, rect)
            if (w < rect.width()) {
                w = rect.width()
            }
        }
        w += 32
        val screenWidth: Int = ScreenUtils.getScreenWidth() / 3
        var offset = screenWidth / w
        if (offset <= 1) offset = 1
        if (offset > 6) offset = 6
        (this.mGridView?.layoutManager as V7GridLayoutManager).spanCount = offset
    }

    override fun showPopupWindow(anchorView: View?) {
        super.showPopupWindow(anchorView)
        popupWindow.isFocusable = true
        val vodInfo = App.instance?.getVodInfo()
        seriesAdapter?.setNewData(vodInfo?.seriesMap?.get(vodInfo.playFlag))
    }

    private fun insertVod(vodInfo: VodInfo) {
        try {
            vodInfo.playNote = vodInfo.seriesMap[vodInfo.playFlag]!![vodInfo.playIndex].name
        } catch (th: Throwable) {
            vodInfo.playNote = ""
        }
        RoomDataManger.insertVodRecord(vodInfo.sourceKey, vodInfo)
        EventBus.getDefault().post(RefreshEvent(RefreshEvent.TYPE_HISTORY_REFRESH))
    }

}