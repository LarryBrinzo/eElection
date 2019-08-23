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
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eelection.PostDataClass
import com.eelection.R
import com.eelection.VideoActivity
import com.eelection.ZoomActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class PostAdapter(private val list: List<PostDataClass>, private val context: Context?) :
    RecyclerView.Adapter<PostAdapter.MyHoder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoder {

        val view = LayoutInflater.from(context).inflate(R.layout.post_card, parent, false)
        return MyHoder(view)
    }

    override fun onBindViewHolder(holder: MyHoder, @SuppressLint("RecyclerView") position: Int) {
        val post = list[position]

        holder.card.setOnClickListener {

            if(post.Type.equals("Video")){

                val intent = Intent(context, VideoActivity::class.java)
                intent.putExtra("PostNumber", post.PostNumber)
                context?.startActivity(intent)
            }

            else if(post.Type.equals("Image")){

                val intent = Intent(context, ZoomActivity::class.java)
                intent.putExtra("PostImage", post.Image)
                context?.startActivity(intent)
            }
        }

        if(post.UserLike.equals("1")){
            holder.likeimg.setImageDrawable(context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.like1) })
            holder.liketext.setTextColor(Color.parseColor("#2866D3"))
        }

        holder.likepost.setOnClickListener{

            val fuser: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
            val userid: String=fuser!!.uid

            if(post.UserLike.equals("0")){

                var databaseReference : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userid).child("Likes").child(post.PostNumber)
                databaseReference.setValue(post.PostNumber)

                holder.likeimg.setImageDrawable(context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.like1) })
                holder.liketext.setTextColor(Color.parseColor("#2866D3"))

                list[position].UserLike="1"
                databaseReference = FirebaseDatabase.getInstance().reference.child("Posts").child(post.PostNumber).child("Likes")
                databaseReference.setValue((post.Likes.toInt()+1).toString())
                list[position].Likes=(post.Likes.toInt()+1).toString()

                if(!post.Likes.equals("0")){
                    holder.likes.setVisibility(View.VISIBLE)
                    holder.limg.setVisibility(View.VISIBLE)
                    holder.hide.setVisibility(View.VISIBLE)
                }
            }

            else{
                var databaseReference : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userid).child("Likes").child(post.PostNumber)
                databaseReference.setValue(null)

                holder.likeimg.setImageDrawable(context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.like) })
                holder.liketext.setTextColor(Color.parseColor("#4D4A4A"))

                list[position].UserLike="0"
                databaseReference = FirebaseDatabase.getInstance().reference.child("Posts").child(post.PostNumber).child("Likes")
                databaseReference.setValue((post.Likes.toInt()-1).toString())
                list[position].Likes=(post.Likes.toInt()-1).toString()

                if(post.Likes.equals("0")){
                    holder.likes.setVisibility(View.GONE)
                    holder.limg.setVisibility(View.GONE)
                    holder.hide.setVisibility(View.GONE)
                }
            }
        }

        if(post.Type.equals("Video"))
            holder.play_button.setVisibility(View.VISIBLE);

        if (context != null) {
            Glide.with(context).load(post.UserImg).apply( RequestOptions.circleCropTransform()).into(holder.userimg)
        }

        if(!post.Type.equals("Text")){

            if (context != null) {
                Glide.with(context).load(post.Image).into(holder.postimg)
            }
        }

        if(post.Likes.equals("0")){
            holder.likes.setVisibility(View.GONE)
            holder.limg.setVisibility(View.GONE)
            holder.hide.setVisibility(View.GONE)
        }

        if(post.Comments.equals("0"))
            holder.comments.setVisibility(View.GONE)

        holder.username.text=post.Username
        holder.designation.text=post.Designation
        holder.partyname.text=post.Party_Name
        holder.posttext.text=post.PostText
        holder.likes.text=post.Likes
        holder.comments.text=post.Comments

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

        var postimg: ImageView
        var userimg: ImageView
        var likeimg: ImageView
        var hide: LinearLayout
        var liketext: TextView
        var play_button: ImageView
        var limg: ImageView
        var username: TextView
        var designation: TextView
        var partyname: TextView
        var posttext: TextView
        var likes: TextView
        var comments: TextView
        var likepost: LinearLayout
        var commentpost: LinearLayout
        var card: LinearLayout

        init {
            postimg = itemView.findViewById(R.id.postimg)
            userimg=itemView.findViewById(R.id.userimg)
            likeimg=itemView.findViewById(R.id.likeimg)
            hide=itemView.findViewById(R.id.hide)
            liketext=itemView.findViewById(R.id.liketext)
            play_button=itemView.findViewById(R.id.play_button)
            limg=itemView.findViewById(R.id.limg)
            card = itemView.findViewById(R.id.card)
            username = itemView.findViewById(R.id.username)
            designation = itemView.findViewById(R.id.designation)
            partyname = itemView.findViewById(R.id.partyname)
            posttext = itemView.findViewById(R.id.posttext)
            likes = itemView.findViewById(R.id.likes)
            comments = itemView.findViewById(R.id.comments)
            likepost = itemView.findViewById(R.id.likepost)
            commentpost = itemView.findViewById(R.id.commentpost)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
