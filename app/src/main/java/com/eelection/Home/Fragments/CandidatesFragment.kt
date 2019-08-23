package com.eelection.Home.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eelection.CampaignDataClass
import com.eelection.CandidatesDataClass
import com.eelection.Home.Adapters.CandidatesAdapter
import com.eelection.Home.Adapters.PostAdapter
import com.eelection.PostDataClass
import com.eelection.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.ArrayList

class CandidatesFragment : Fragment() {

    var cadidateslist: MutableList<CandidatesDataClass> = ArrayList()
    lateinit var postAdapter : CandidatesAdapter
    lateinit var candidatesrecycle : RecyclerView

    companion object {

        fun newInstance(): HomeFragment {
            val fragmentHome = HomeFragment()
            val args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater!!.inflate(R.layout.frag_candidates, container, false)

        candidatesrecycle = view.findViewById(R.id.candidatesrecycle)

        postAdapter = CandidatesAdapter(cadidateslist, context)
        val recycepost = GridLayoutManager(context, 1)
        candidatesrecycle.setLayoutManager(recycepost)
        recycepost.isAutoMeasureEnabled = false
        candidatesrecycle.setItemAnimator(DefaultItemAnimator())
        candidatesrecycle .setAdapter(postAdapter)

        nearCandidates()

        return view
    }


    fun nearCandidates() {

        val databaseReference : DatabaseReference  = FirebaseDatabase.getInstance().reference.child("Candidates")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                cadidateslist.clear()

                for (dataSnapshot1 in dataSnapshot.children) {

                    val candidate= CandidatesDataClass( dataSnapshot1.child("Username").getValue().toString(),
                        dataSnapshot1.child("UserImg").getValue().toString(),
                                dataSnapshot1.child("Designation").getValue().toString(),
                        dataSnapshot1.child("Party_Name").getValue().toString(),
                        dataSnapshot1.child("Constituency").getValue().toString(),
                        dataSnapshot1.child("User_Info").getValue().toString(),
                        dataSnapshot1.key.toString())

                    cadidateslist.add(candidate)
                    postAdapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

}