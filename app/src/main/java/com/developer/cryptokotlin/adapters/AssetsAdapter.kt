package com.developer.cryptokotlin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer.cryptokotlin.R
import com.developer.cryptokotlin.connect.models.AssetObject
import com.developer.cryptokotlin.databinding.ItemAssetsBinding
import com.developer.cryptokotlin.utils.Constants
import com.developer.cryptokotlin.utils.Utils


class AssetsAdapter(private val clickListener: AssetClickListener) : PagedListAdapter<AssetObject, AssetsAdapter.ViewHolder>(
    DiffCallback()
) {


    class ViewHolder private constructor(private val binding: ItemAssetsBinding) : RecyclerView.ViewHolder(
        binding.root
    ){

        fun bind(item: AssetObject, clickListener: AssetClickListener) {
            setData(item)
            binding.linearLayout.setOnClickListener { clickListener.onClicked(item) }
            binding.executePendingBindings()
        }

        @SuppressLint("SetTextI18n")
        fun setData(item: AssetObject) {
            binding.textViewName.text = item.name
            binding.textViewOrder.text = (layoutPosition + 1).toString()
            binding.textViewPrice.text =
                "$" + Utils.formatBalance(item.metrics!!.market_data!!.price_usd)
            binding.textViewStatus.text =
                Utils.formatBalance(item.metrics!!.market_data!!.percent_change_usd_last_24_hours) + "%"
            binding.textViewBalanceUnit.text =
                Utils.formatUnit(item.metrics!!.marketcap!!.current_marketcap_usd)
            binding.textViewAllAmount.text =
                Utils.roundUnit(item.metrics!!.marketcap!!.current_marketcap_usd)
            Glide.with(itemView.context)
                .load(Constants.ASSET_IMAGES + item.id + "/128.png")
                .into(binding.imageView)


            if (item.metrics!!.market_data!!.percent_change_usd_last_24_hours > 0) {
                binding.textViewStatus.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.profit_color
                    )
                )
            } else {
                binding.textViewStatus.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.red
                    )
                )
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAssetsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, clickListener)
    }


}

class DiffCallback : DiffUtil.ItemCallback<AssetObject>() {

    override fun areItemsTheSame(oldItem: AssetObject, newItem: AssetObject): Boolean {
        return oldItem.name == newItem.name
    }


    override fun areContentsTheSame(oldItem: AssetObject, newItem: AssetObject): Boolean {
        return oldItem == newItem
    }


}

class AssetClickListener(val clickListener: (asset: AssetObject) -> Unit) {
    fun onClicked(asset: AssetObject) = clickListener(asset)
}