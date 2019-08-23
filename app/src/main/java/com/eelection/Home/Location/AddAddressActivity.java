package com.eelection.Home.Location;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eelection.R;


public class AddAddressActivity extends AppCompatActivity {

    EditText compadd,landmark;
    TextView change,add,myconsttext,othertext;
    Button next;
    LinearLayout myconstituency,other;
    int myconst=0,oth=0;
    String addid;
    LinearLayout back;

    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        fa = this;

        add=findViewById(R.id.addname);
        compadd=findViewById(R.id.compadd);
        landmark=findViewById(R.id.landmark);
        change=findViewById(R.id.change);
        next=findViewById(R.id.next);
        myconstituency=findViewById(R.id.myconst);
        other=findViewById(R.id.other);
        myconsttext=findViewById(R.id.consttext);
        othertext=findViewById(R.id.othertext);
        back=findViewById(R.id.backbt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String uadd=pref.getString("useraddress", null);

        add.setText(uadd);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        addid=pref.getString("useraddresssid", null);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey("addname"))
                add.setText(bundle.getString("addname"));

            if (bundle.containsKey("addid"))
                addid=(bundle.getString("addid"));

            else
                add.setText(uadd);
        }

        myconstituency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(myconst==0){
                    myconstituency.setBackgroundResource(R.drawable.nicknameborder2);
                    myconsttext.setTextColor(Color.parseColor("#ffffff"));
                    myconst=1;
                }
                else{
                    myconstituency.setBackgroundResource(R.drawable.nicknameborder);
                    myconsttext.setTextColor(Color.parseColor("#10a115"));
                    myconst=0;
                }

                othertext.setTextColor(Color.parseColor("#10a115"));
                other.setBackgroundResource(R.drawable.nicknameborder);
                oth=0;

                if(compadd.getText().toString().length()>0 && (myconst==1 || oth==1)){
                    next.setEnabled(true);
                    next.setBackgroundResource(R.drawable.border3);
                    next.setTextColor(Color.parseColor("#ffffff"));
                }

                else {
                    next.setEnabled(false);
                    next.setBackgroundResource(R.drawable.border8);
                    next.setTextColor(Color.parseColor("#aeaeae"));
                }

            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oth==0){
                    other.setBackgroundResource(R.drawable.nicknameborder2);
                    othertext.setTextColor(Color.parseColor("#ffffff"));
                    oth=1;
                }
                else{
                    other.setBackgroundResource(R.drawable.nicknameborder);
                    othertext.setTextColor(Color.parseColor("#10a115"));
                    oth=0;
                }

                myconsttext.setTextColor(Color.parseColor("#10a115"));
                myconstituency.setBackgroundResource(R.drawable.nicknameborder);
                myconst=0;

                if(compadd.getText().toString().length()>0 && (myconst==1 || oth==1)){
                    next.setEnabled(true);
                    next.setBackgroundResource(R.drawable.border3);
                    next.setTextColor(Color.parseColor("#ffffff"));
                }

                else {
                    next.setEnabled(false);
                    next.setBackgroundResource(R.drawable.border8);
                    next.setTextColor(Color.parseColor("#aeaeae"));
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAddressActivity.this, GetUsersLocationActivity.class);
                intent.putExtra("change", "1");
                startActivity(intent);
            }
        });

        compadd.requestFocus();

        compadd.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence st, int start, int before, int count) {

                String newText = st.toString();

                if(newText.length()==0){
                    next.setEnabled(false);
                    next.setBackgroundResource(R.drawable.border8);
                    next.setTextColor(Color.parseColor("#aeaeae"));
                }

                newText=newText.trim();
                newText=newText.toLowerCase();

                if(newText.length()>1 && (myconst==1 || oth==1) ) {
                    next.setEnabled(true);
                    next.setBackgroundResource(R.drawable.border3);
                    next.setTextColor(Color.parseColor("#ffffff"));
                }

            }


        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAddressActivity.this, MarkYourLocationActivity.class);
                intent.putExtra("complete_address",compadd.getText().toString());

                if(landmark.getText().toString().length()>0)
                    intent.putExtra("landmark",landmark.getText().toString());

                if(myconst==1)
                    intent.putExtra("nickname","MY CONSTITUENCY");
                else if(oth==1)
                    intent.putExtra("nickname","OTHER");

                intent.putExtra("addname",add.getText().toString());
                intent.putExtra("addid",addid);

                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
