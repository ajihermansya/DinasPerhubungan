package com.dinas.perhubungan.adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dinas.perhubungan.R
import com.dinas.perhubungan.data.model.JabatanModel
import com.dinas.perhubungan.databinding.UserItemLayoutBinding
import com.dinas.perhubungan.ui.menu_admin.detail_jabatan.DetailJabatanActivity


class ListUserAdapter(var context: Context, var list: ArrayList<JabatanModel>)
    : RecyclerView.Adapter<ListUserAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var binding : UserItemLayoutBinding = UserItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item_layout, parent, false))
    }
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
       var user = list[position]
        holder.binding.nameList.text = user.nama
        holder.binding.number.text = user.no.toString()

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailJabatanActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}

