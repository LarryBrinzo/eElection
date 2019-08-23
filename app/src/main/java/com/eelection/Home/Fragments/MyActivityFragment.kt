package com.eelection.Home.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eelection.Home.Adapters.PostAdapter
import com.eelection.PostDataClass
import com.eelection.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.ArrayList

class MyActivityFragment : Fragment() {

    var likelist: MutableList<String> = ArrayList()
    var postlist: MutableList<PostDataClass> = ArrayList()
    lateinit var postAdapter : PostAdapter
    lateinit var postrecycle : RecyclerView

    companion object {

        fun newInstance(): HomeFragment {
            val fragmentHome = HomeFragment()
            val args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater!!.inflate(R.layout.frag_activity, container, false)

        postrecycle = view.findViewById(R.id.postrecycle)

        postAdapter = PostAdapter(postlist, context)
        val recycepost = GridLayoutManager(context, 1)
        postrecycle.setLayoutManager(recycepost)
        recycepost.isAutoMeasureEnabled = false
        postrecycle.setItemAnimator(DefaultItemAnimator())
        postrecycle .setAdapter(postAdapter)

        userLikes()

        return view
    }


    fun userLikes() {

        val fuser: FirebaseUser? =FirebaseAuth.getInstance().getCurrentUser()
        val userid: String=fuser!!.uid

        val databaseReference :DatabaseReference= FirebaseDatabase.getInstance().reference.child("Users").child(userid).child("Likes")

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

        val databaseReference : DatabaseReference  = FirebaseDatabase.getInstance().reference.child("Posts")

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

}