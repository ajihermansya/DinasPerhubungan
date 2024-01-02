package com.dinas.perhubungan.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dinas.perhubungan.R
import com.dinas.perhubungan.data.model.JabatanModel
import com.dinas.perhubungan.databinding.ChatUserItemLayoutBinding


class ChatAdapter(var context: Context, var list: ArrayList<JabatanModel>)
    : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var binding : ChatUserItemLayoutBinding = ChatUserItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_user_item_layout, parent, false))
    }
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
       var user = list[position]
        holder.binding.number.text = user.no
        //holder.binding.userName.text = user.name
//
//        holder.itemView.setOnClickListener {
//            val intent = Intent(context, DetailJabatanActivity::class.java)
//            intent.putExtra("uid", user.no)
//            context.startActivity(intent)
//        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}

