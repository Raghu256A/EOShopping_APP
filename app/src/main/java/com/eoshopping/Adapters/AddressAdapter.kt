package com.eoshopping.Adapters

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eoshopping.R
import com.eoshopping.pojo.AddressDo

class AddressAdapter(private var address: List<AddressDo>) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ll_add_view_header: LinearLayout = view.findViewById(R.id.ll_add_view_header)
        val chck_confirm: CheckBox = view.findViewById(R.id.chck_confirm)
        val tv_addType: TextView = view.findViewById(R.id.tv_addType)
        val tv_full_address: TextView = view.findViewById(R.id.tv_full_address)
        val tv_delivery_Inst: TextView = view.findViewById(R.id.tv_delivery_Inst)
        val tv_curt_location: TextView = view.findViewById(R.id.tv_curt_location)
        val tv_editAdd: TextView = view.findViewById(R.id.tv_editAdd)
        val tv_RemoveAdd: TextView = view.findViewById(R.id.tv_RemoveAdd)
        val tv_setDeftAdd: TextView = view.findViewById(R.id.tv_setDeftAdd)
        val tv_add_view_Name: TextView = view.findViewById(R.id.tv_add_view_Name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.address_view, parent, false)

        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return address.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val list = address[position]
        if (list.IsDefaultAdd == "1") {
            holder.chck_confirm.isChecked = true
            holder.chck_confirm.isEnabled = false
        } else {
            holder.chck_confirm.isChecked = false
            holder.chck_confirm.isEnabled = false
        }
        holder.tv_addType.text = list.AddressType
        holder.tv_add_view_Name.text = list.Name
        holder.tv_full_address.text =
            "${list.MobileNumber}\n${list.FullAddress} ${list.Area} \n${list.LandMark} ${list.TownOrCity} \n${list.State} ${list.Country} ${list.PineCode}"

    }

    fun updateItems(newItems: List<AddressDo>) {
        address = newItems
        notifyDataSetChanged()
    }
}