package com.eelection.Home.Adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eelection.*
import com.eelection.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class ElectionAdapter(private val list: List<CandidatesDataClass>, private val context: Context?) :
    RecyclerView.Adapter<ElectionAdapter.MyHoder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoder {

        val view = LayoutInflater.from(context).inflate(R.layout.election_card, parent, false)
        return MyHoder(view)
    }

    override fun onBindViewHolder(holder: MyHoder, @SuppressLint("RecyclerView") position: Int) {
        val post = list[position]

        holder.vote.setOnClickListener {

            val fuser: FirebaseUser? =FirebaseAuth.getInstance().getCurrentUser()
            val userid: String=fuser!!.uid

            val databaseReference :DatabaseReference= FirebaseDatabase.getInstance().reference.child("Users").child(userid).child("Vote")

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.getValue().toString().equals("1")){

                    }
                    else{

                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Are you sure. You want to vote?")
                            .setCancelable(false)
                            .setPositiveButton("Yes") { dialog, id ->

                                val Reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userid).child("Vote")
                                Reference.setValue("1")
                                Toast.makeText(context,"Vote Recorded!",Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("No") { dialog, id ->
                            }
                        val alert = builder.create()
                        alert.show()

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        }

        if (context != null) {
            Glide.with(context).load(post.UserImg).apply( RequestOptions.circleCropTransform()).into(holder.userimg)
        }

        when(post.Party_Name){
            "BJP" -> holder.partyimg?.let {
                if (context != null) {
                    Glide.with(context).load(getImage("bjp")).into(it)
                }
            }
            "INC" -> holder.partyimg?.let {
                if (context != null) {
                    Glide.with(context).load(getImage("inc")).into(it)
                }
            }
            "BSP" -> holder.partyimg?.let {
                if (context != null) {
                    Glide.with(context).load(getImage("bsp")).into(it)
                }
            }
            "CPI" -> holder.partyimg?.let {
                if (context != null) {
                    Glide.with(context).load(getImage("cpi")).into(it)
                }
            }
        }

        holder.username.text=post.Username
        holder.designation.text=post.Designation
    }

    fun getImage(imageName: String): Int {

        return context!!.resources.getIdentifier(imageName, "drawable", context.packageName)
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
        var partyimg: ImageView
        var username: TextView
        var designation: TextView
        var constituency: TextView
        var card: LinearLayout
        var vote: Button

        init {
            userimg=itemView.findViewById(R.id.userimg)
            vote=itemView.findViewById(R.id.vote)
            partyimg=itemView.findViewById(R.id.partyimg)
            username = itemView.findViewById(R.id.username)
            designation = itemView.findViewById(R.id.designation)
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
