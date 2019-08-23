package com.eelection.MyAccount

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.eelection.Home.Adapters.PostAdapter
import com.eelection.PostDataClass
import com.eelection.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.ArrayList

class MyActivityActivity : AppCompatActivity() {

    var likelist: MutableList<String> = ArrayList()
    var postlist: MutableList<PostDataClass> = ArrayList()
    lateinit var postAdapter : PostAdapter
    lateinit var postrecycle : RecyclerView
    lateinit var back : LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_activity)


        postrecycle = findViewById(R.id.postrecycle)
        back = findViewById(R.id.backbt)

        back.setOnClickListener { onBackPressed() }

        postAdapter = PostAdapter(postlist, applicationContext)
        val recycepost = GridLayoutManager(applicationContext, 1)
        postrecycle.setLayoutManager(recycepost)
        recycepost.isAutoMeasureEnabled = false
        postrecycle.setItemAnimator(DefaultItemAnimator())
        postrecycle .setAdapter(postAdapter)

        userLikes()
    }

    fun userLikes() {

        val fuser: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
        val userid: String=fuser!!.uid

        val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userid).child("Likes")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                likelist.clear()
                for (dataSnapshot1 in dataSnapshot.children) {
                    likelist.add(dataSnapshot1.getValue().toString())
                }

                partyPost()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }


    fun partyPost() {

        val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Posts")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                postlist.clear()

                for (ind: Int in likelist.indices) {

                    val post : PostDataClass? = dataSnapshot.child(likelist[ind]).getValue<PostDataClass>(PostDataClass::class.java)
                    post!!.PostNumber=dataSnapshot.child(likelist[ind]).key.toString()

                    if(likelist.contains(post.PostNumber))
                        post.UserLike="1"
                    else
                        post.UserLike="0"

                    postlist.add(post)
                    postAdapter.notifyDataSetChanged()

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
