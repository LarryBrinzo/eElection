package com.eelection.LoginReister

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.eelection.Home.BottomNaviagtionDrawer
import com.eelection.LoginReister.PrefManger.PrefManager
import com.eelection.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class MobileVerificationActivity : AppCompatActivity() {

    var aadhaarNumber: String?=null
    var mobileNumber: String?=null
    var code_sent: String?=null
    lateinit var otp: EditText
    lateinit var verify: Button
    lateinit var back: LinearLayout
    lateinit var resend: TextView
    private var prefManager: PrefManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_verification)

        prefManager = PrefManager(this)
        if (!prefManager!!.isFirstTimeLaunch()) {
            launchHomeScreen()
            finishAffinity()
        }

        resend=findViewById(R.id.resend)
        verify=findViewById(R.id.verify)
        otp=findViewById(R.id.otp)
        back = findViewById(R.id.backbt)

        back.setOnClickListener { onBackPressed() }

        resend.setOnClickListener { Send_code() }

        val bundle = intent.extras
        if (bundle != null) {
            aadhaarNumber = bundle.getString("AadhaarNumber")
            aadhaarDetails()
        }

        otp.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val pr = s.toString()

                if (pr.length == 6) {
                    verify.setEnabled(true)
                    verify.setBackgroundResource(R.drawable.border3)
                } else{
                    verify.setEnabled(false)
                    verify.setBackgroundResource(R.drawable.border8)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int) {
            }
            override fun afterTextChanged(s: Editable) {}
        })

        verify.setOnClickListener {
            if(code_sent!=null)
                Verify_signincodeNewUser()
        }
    }

    fun aadhaarDetails() {

        val databaseReference =
            aadhaarNumber?.let { FirebaseDatabase.getInstance().reference.child("Aadhaars").child(it).child("Mobile") }

        databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mobileNumber=dataSnapshot.getValue().toString()
                Send_code()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    fun Send_code() {

        val Number = "+91$mobileNumber"

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            Number,
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }


    internal var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                code_sent = s
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                //if(phoneAuthCredential!=null)
                //  phupdate(phoneAuthCredential,0);
            }

            override fun onVerificationFailed(e: FirebaseException) {
            }

        }

    private fun Verify_signincodeNewUser() {

        val code_en = otp.text.toString()

        if(code_sent!=null){
        val credential = PhoneAuthProvider.getCredential(code_sent!!, code_en)
        signInWithPhoneAuthCredential(credential)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this@MobileVerificationActivity,
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {

                        val fuser : FirebaseUser ?= FirebaseAuth.getInstance().getCurrentUser()

                        if(fuser!=null){

                        val userid = FirebaseAuth.getInstance().getCurrentUser()!!.getUid()
                        val databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userid)

                        databaseReference.child("Aadhaar_Number").setValue(aadhaarNumber)

                        val intent = Intent(applicationContext, GetLocationActivity::class.java)
                        startActivity(intent)

                        }

                        else {
                            Toast.makeText(applicationContext,"Verification failed",Toast.LENGTH_SHORT).show()
                        }

                    } else {

                        if (task.exception is FirebaseAuthUserCollisionException) {

                        } else {
                           Toast.makeText(applicationContext,"Incorrect verification code entered.",Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }

    private fun launchHomeScreen() {
        prefManager!!.setFirstTimeLaunch(false)
        startActivity(Intent(applicationContext, BottomNaviagtionDrawer::class.java))
        finishAffinity()
    }

}
