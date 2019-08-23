package com.eelection.Home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout

import com.eelection.R

class ContactUsActivity : AppCompatActivity() {

    lateinit var call : LinearLayout
    lateinit var email : LinearLayout
    lateinit var feed : LinearLayout
    lateinit var back : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        call = findViewById(R.id.calll)
        email = findViewById(R.id.emaill)
        feed = findViewById(R.id.feed)

        back = findViewById(R.id.backbt)

        back.setOnClickListener { onBackPressed() }

        feed.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(" ")
            startActivity(openURL)
        }

        call.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(this@ContactUsActivity)
            builder.setMessage("Are you sure that you have a query. Press call to contact us.")
                .setCancelable(false)
                .setPositiveButton("Call", DialogInterface.OnClickListener { dialog, id ->
                    val i = Intent(Intent.ACTION_CALL)
                    i.data = Uri.parse("tel:" + "7632002034")
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            android.Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return@OnClickListener
                    }
                    startActivity(i)
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> })
            val alert = builder.create()
            alert.show()
        })

        email.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "text/plain"
            intent.data = Uri.parse("mailto:quidish.co@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Having an issue")
            startActivity(Intent.createChooser(intent, "Send Email"))
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
