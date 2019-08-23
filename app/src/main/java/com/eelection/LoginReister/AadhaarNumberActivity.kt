package com.eelection.LoginReister

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.eelection.Home.BottomNaviagtionDrawer
import com.eelection.LoginReister.PrefManger.PrefManager
import com.eelection.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AadhaarNumberActivity : AppCompatActivity() {

    lateinit var aadhaartext: TextView
    lateinit var next: Button
    private var prefManager: PrefManager? = null
    lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_aadhaar)

        getWindow().setBackgroundDrawableResource(R.drawable.vote)

        prefManager = PrefManager(this)
        if (!prefManager!!.isFirstTimeLaunch()) {
            launchHomeScreen()
            finishAffinity()
        }

        aadhaartext=findViewById(R.id.anumber)
        next=findViewById(R.id.next)
        progressbar=findViewById(R.id.progbar)


        aadhaartext.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val pr = s.toString()

                if (pr.length == 12) {
                    next.setEnabled(true)
                    next.setBackgroundResource(R.drawable.oval_button_green)
                } else{
                    next.setEnabled(false)
                    next.setBackgroundResource(R.drawable.oval_button_grey)
                }
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {}
        })

        next.setOnClickListener{
            progressbar.setVisibility(View.VISIBLE)
            aadhaarCheck()
        }
    }

    fun aadhaarCheck() {

        val databaseReference =
            FirebaseDatabase.getInstance().reference.child("Aadhaars").child((aadhaartext.text.toString()).replace("\\s", ""))

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                progressbar.setVisibility(View.GONE)
                val intent = Intent(applicationContext, MobileVerificationActivity::class.java)
                intent.putExtra("AadhaarNumber",(aadhaartext.text.toString()).replace("\\s", ""))
                startActivity(intent)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext,"Aadhaar Number not found!",Toast.LENGTH_SHORT).show()
                progressbar.setVisibility(View.GONE)
            }
        })
    }

    private fun launchHomeScreen() {
        prefManager!!.setFirstTimeLaunch(false)
        startActivity(Intent(applicationContext, BottomNaviagtionDrawer::class.java))
        finishAffinity()
    }
}
