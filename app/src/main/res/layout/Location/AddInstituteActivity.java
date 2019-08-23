package com.quidish.anshgupta.login.Home.Location;

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
import com.quidish.anshgupta.login.Home.BottomNavifation.HomeFragment;
import com.quidish.anshgupta.login.R;

public class AddInstituteActivity extends AppCompatActivity {

    EditText compadd,landmark;
    TextView change,inst,ontext,offtext;
    Button next;
    LinearLayout oncapus,offcampus;
    int oncamp=0,offcamp=0;
    int post=1;
    String instid;
    LinearLayout back;

    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_institute);

        fa = this;

        inst=findViewById(R.id.instname);
        compadd=findViewById(R.id.compadd);
        landmark=findViewById(R.id.landmark);
        change=findViewById(R.id.change);
        next=findViewById(R.id.next);
        oncapus=findViewById(R.id.oncampus);
        offcampus=findViewById(R.id.offcampus);
        ontext=findViewById(R.id.ontext);
        offtext=findViewById(R.id.offtext);
        back=findViewById(R.id.backbt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        inst.setText(HomeFragment.userclg);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        instid=pref.getString("userinstituteid", null);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey("instname"))
                inst.setText(bundle.getString("instname"));

            if (bundle.containsKey("instid"))
                instid=(bundle.getString("instid"));

            if (bundle.containsKey("post"))
               post=1;

            else
                inst.setText(HomeFragment.userclg);
        }

        oncapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(oncamp==0){
                    oncapus.setBackgroundResource(R.drawable.nicknameborder2);
                    ontext.setTextColor(Color.parseColor("#ffffff"));
                    oncamp=1;
                }
                else{
                    oncapus.setBackgroundResource(R.drawable.nicknameborder);
                    ontext.setTextColor(Color.parseColor("#10a115"));
                    oncamp=0;
                }

                offtext.setTextColor(Color.parseColor("#10a115"));
                offcampus.setBackgroundResource(R.drawable.nicknameborder);
                offcamp=0;

                if(compadd.getText().toString().length()>0 && (oncamp==1 || offcamp==1)){
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

        offcampus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(offcamp==0){
                    offcampus.setBackgroundResource(R.drawable.nicknameborder2);
                    offtext.setTextColor(Color.parseColor("#ffffff"));
                    offcamp=1;
                }
                else{
                    offcampus.setBackgroundResource(R.drawable.nicknameborder);
                    offtext.setTextColor(Color.parseColor("#10a115"));
                    offcamp=0;
                }

                ontext.setTextColor(Color.parseColor("#10a115"));
                oncapus.setBackgroundResource(R.drawable.nicknameborder);
                oncamp=0;

                if(compadd.getText().toString().length()>0 && (oncamp==1 || offcamp==1)){
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
                Intent intent = new Intent(com.quidish.anshgupta.login.Home.Location.AddInstituteActivity.this, com.quidish.anshgupta.login.Home.Location.GetUsersLocationActivity.class);
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

                if(newText.length()>1 && (oncamp==1 || offcamp==1) ) {
                    next.setEnabled(true);
                    next.setBackgroundResource(R.drawable.border3);
                    next.setTextColor(Color.parseColor("#ffffff"));
                }

            }


        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.quidish.anshgupta.login.Home.Location.AddInstituteActivity.this, com.quidish.anshgupta.login.Home.Location.MarkYourLocationActivity.class);
                intent.putExtra("complete_address",compadd.getText().toString());

                if(landmark.getText().toString().length()>0)
                    intent.putExtra("landmark",landmark.getText().toString());

                if(oncamp==1)
                    intent.putExtra("nickname","On-Campus");
                else if(offcamp==1)
                    intent.putExtra("nickname","Off-Campus");

                intent.putExtra("inst_name",inst.getText().toString());
                intent.putExtra("instid",instid);

                if(post==1)
                    intent.putExtra("post","1");

                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
