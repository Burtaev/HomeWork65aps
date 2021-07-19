package com.burtaev.application

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

private const val PADDING_TOP_IN_DP = 8
private const val ITEM_PADDING_IN_DP = 12

class ItemsDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val startSpace = view.dp2px(PADDING_TOP_IN_DP)
        outRect.top = when (parent.getChildLayoutPosition(view)) {
            0 -> startSpace
            else -> view.dp2px(ITEM_PADDING_IN_DP)
        }
        outRect.bottom =
            if (parent.getChildLayoutPosition(view) == state.itemCount - 1) startSpace else 0
    }

    @Px
    private fun View.dp2px(@Dimension(unit = Dimension.DP) value: Int) = context.dp2px(value)

    @Px
    private fun Context.dp2px(@Dimension(unit = Dimension.DP) value: Int) =
        value * resources.displayMetrics.density.toInt()
}