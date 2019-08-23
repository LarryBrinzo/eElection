package com.eelection.Home.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eelection.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class CandidatesAdapter(private val list: List<CandidatesDataClass>, private val context: Context?) :
    RecyclerView.Adapter<CandidatesAdapter.MyHoder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoder {

        val view = LayoutInflater.from(context).inflate(R.layout.candidates_card, parent, false)
        return MyHoder(view)
    }

    override fun onBindViewHolder(holder: MyHoder, @SuppressLint("RecyclerView") position: Int) {
        val post = list[position]

        holder.card.setOnClickListener {
            if(holder.down==0){
                holder.down=1
                holder.userinfo.setVisibility(View.VISIBLE)
                holder.downarrow.setImageDrawable(context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.up) })
            }

            else{
                holder.down=0
                holder.userinfo.setVisibility(View.GONE)
                holder.downarrow.setImageDrawable(context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.locdown) })

            }
        }

        if (context != null) {
            Glide.with(context).load(post.UserImg).apply( RequestOptions.circleCropTransform()).into(holder.userimg)
        }

        holder.username.text=post.Username
        holder.designation.text=post.Designation
        holder.partyname.text=post.Party_Name
        holder.userinfo.text=post.User_Info
    }

    override fun getItemCount(): Int {

        var arr = 0

        try {
            if (list.size == 0) {

                arr = 0

            } else {

                arr = list.size
            }


        } catch (ignored: Exception) {


        }

        return arr

    }

    inner class MyHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userimg: ImageView
        var downarrow: ImageView
        var username: TextView
        var designation: TextView
        var partyname: TextView
        var userinfo: TextView
        var constituency: TextView
        var card: LinearLayout
        var down: Int=0

        init {
            userimg=itemView.findViewById(R.id.userimg)
            username = itemView.findViewById(R.id.username)
            designation = itemView.findViewById(R.id.designation)
            partyname = itemView.findViewById(R.id.partyname)
            downarrow = itemView.findViewById(R.id.downarrow)
            userinfo = itemView.findViewById(R.id.userinfo)
            constituency = itemView.findViewById(R.id.constituency)
            card=itemView.findViewById(R.id.card)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
