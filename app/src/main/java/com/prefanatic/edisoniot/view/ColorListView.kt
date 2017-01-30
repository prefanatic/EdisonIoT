package com.prefanatic.edisoniot.view

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.prefanatic.edisoniot.convertDpToPixel
import rx.subjects.PublishSubject
import java.util.*

/**
 * Created by cody on 1/30/17.
 */
class ColorListView : RecyclerView {
    val clickSubject = PublishSubject.create<Int>()

    private val adapter: Adapter
        get() = getAdapter() as Adapter

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context) {
        setAdapter(Adapter())
        layoutManager = GridLayoutManager(context, 4)
    }

    fun addColor(@ColorInt color: Int) {
        adapter.data.add(color)
        adapter.notifyItemInserted(adapter.data.size)
    }

    inner class Adapter(val data: MutableList<Int> = ArrayList())
        : RecyclerView.Adapter<ViewHolder>() {

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val color = data[position]

            holder?.itemView?.setBackgroundColor(color)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val holder = ViewHolder(FrameLayout(parent!!.context).apply {
                val padding = convertDpToPixel(64f, parent.context).toInt()
                setPadding(padding, padding, padding, padding)
            })

            holder.itemView.setOnClickListener {
                val col = data[holder.adapterPosition]
                clickSubject.onNext(col)
            }

            return holder
        }

        override fun getItemCount() = data.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}
