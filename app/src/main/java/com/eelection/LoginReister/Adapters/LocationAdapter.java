package com.eelection.LoginReister.Adapters;

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
import com.eelection.Home.BottomNaviagtionDrawer;
import com.eelection.Home.Location.AddAddressActivity;
import com.eelection.Home.Location.GetUsersLocationActivity;
import com.eelection.LoginReister.PrefManger.PrefManager;
import com.eelection.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyHoder>{

    private List<Pair<String,String>> list;
    private Context context;
    private PrefManager prefManager;

    public LocationAdapter(List<Pair<String,String>> list, Context context) {
        this.list = list;
        this.context = context;
        prefManager = new PrefManager(context);
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

                if(GetUsersLocationActivity.changelay==0){
                SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor searchhint = pref.edit();

                searchhint.putString("useraddress",list.get(position).first);
                searchhint.putString("useraddressid",list.get(position).second);

                int instnum=pref.getInt("addressnumber", 0)+1;
                String srchitm="recentadd"+Integer.toString(instnum);
                searchhint.putString(srchitm,list.get(position).first);

                srchitm="recentaddid"+Integer.toString(instnum);
                searchhint.putString(srchitm,list.get(position).second);

                searchhint.putInt("addressnumber",instnum);
                searchhint.apply();

                prefManager.setFirstTimeLaunch(false);
                Intent intent=new Intent(context, BottomNaviagtionDrawer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity)context).finishAffinity();

                }

                else{
                    Intent intent=new Intent(context, AddAddressActivity.class);
                    intent.putExtra("addname", list.get(position).first);
                    intent.putExtra("addid", list.get(position).second);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
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




