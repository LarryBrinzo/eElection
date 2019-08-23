package com.eelection;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eelection.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ZoomActivity extends AppCompatActivity {

    String pic;
    ImageView image;
    LinearLayout back;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        image=findViewById(R.id.image);
        back=findViewById(R.id.backbt);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pic = bundle.getString("PostImage");
        }

        RequestOptions options = new RequestOptions();
        options.fitCenter();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

      //  Picasso.get().load(pic).into(image);
        Glide.with(getApplicationContext()).load(pic).apply(options).into(image);

        image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(image);
        pAttacher.update();


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
