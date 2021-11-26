package com.griname.foody.util

import androidx.recyclerview.widget.DiffUtil
import com.griname.foody.model.Result

class RecipesDiffUtil(
    private val oldData: List<Result>,
    private val newData: List<Result>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldData[oldItemPosition] === newData[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldData[oldItemPosition] == newData[newItemPosition]
}