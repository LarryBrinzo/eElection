package com.eelection.Home.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.eelection.Home.ContactUsActivity
import com.eelection.Home.Location.GetUsersLocationActivity
import com.eelection.MyAccount.MyActivityActivity
import com.eelection.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    lateinit var activity : LinearLayout
    lateinit var savedadd : LinearLayout
    lateinit var contact : LinearLayout
    lateinit var rate : LinearLayout
    lateinit var aadhaar : TextView
    lateinit var name : TextView
    lateinit var dob : TextView
    lateinit var address : TextView
    var aadhaar_number: String=null ?: "11111111111"

    companion object {

        fun newInstance(): HomeFragment {
            val fragmentHome = HomeFragment()
            val args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater!!.inflate(R.layout.frag_profile, container, false)

        activity = view.findViewById(R.id.activity)
        contact = view.findViewById(R.id.contact)
        rate = view.findViewById(R.id.rate)
        savedadd = view.findViewById(R.id.savedadd)
        aadhaar=view.findViewById(R.id.aadhaarnumber)
        name=view.findViewById(R.id.name)
        dob=view.findViewById(R.id.dob)
        address=view.findViewById(R.id.address)


        activity.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, MyActivityActivity::class.java)
            startActivity(intent)
        })

        contact.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ContactUsActivity::class.java)
            startActivity(intent)
        })


        rate.setOnClickListener(View.OnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse(" ")
            startActivity(openURL)
        })

        savedadd.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, GetUsersLocationActivity::class.java)
            startActivity(intent)
        })

        linkaccount()

        return view
    }

    fun linkaccount() {

        val fuser: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
        val userid: String=fuser!!.uid
        val databaseReference : DatabaseReference  = FirebaseDatabase.getInstance().reference.child("Users").child(userid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user_aadhaar =
                    dataSnapshot.child("Aadhaar_Number").getValue().toString()

                aadhaar_number=user_aadhaar

                linkAadhaar()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    fun linkAadhaar() {

        val databaseReference : DatabaseReference  = FirebaseDatabase.getInstance().reference.child("Aadhaars").child(aadhaar_number)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user_aadhaar =
                    dataSnapshot.child("Aadhaar_Number").getValue().toString()
                val user_name =
                    dataSnapshot.child("Name").getValue().toString()
                val user_dob =
                    dataSnapshot.child("DOB").getValue().toString()
                val user_add =
                    dataSnapshot.child("Address").getValue().toString()

                aadhaar.text=user_aadhaar
                name.text=user_name
                dob.text=user_dob
                address.text=user_add
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

}