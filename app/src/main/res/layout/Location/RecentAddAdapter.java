package com.quidish.anshgupta.login.Home.Location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.quidish.anshgupta.login.Home.BottomNavifation.BottomNavigationDrawerActivity;
import com.quidish.anshgupta.login.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecentAddAdapter extends RecyclerView.Adapter<com.quidish.anshgupta.login.Home.Location.RecentAddAdapter.MyHoder>{

    private List<Pair<String,String>> list;
    private Context context;

    public RecentAddAdapter(List<Pair<String,String>> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MyHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.location_suggestion_layout,parent,false);
        return new MyHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHoder holder, @SuppressLint("RecyclerView") final int position) {

        holder.suggestion.setText(list.get(position).first);

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor searchhint = pref.edit();

                searchhint.putString("userinstitute",list.get(position).first);
                searchhint.putString("userinstituteid",list.get(position).second);

                searchhint.apply();

                Intent intent=new Intent(context, BottomNavigationDrawerActivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try{
            if(list.size()==0){

                arr = 0;

            }
            else{

                arr=list.size();
            }



        }catch (Exception ignored){



        }

        return arr;

    }


    class MyHoder extends RecyclerView.ViewHolder{
        TextView suggestion;
        LinearLayout cardview;

        MyHoder(View itemView) {
            super(itemView);
            suggestion = itemView.findViewById(R.id.sugg);
            cardview=itemView.findViewById(R.id.card_view);

        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}




