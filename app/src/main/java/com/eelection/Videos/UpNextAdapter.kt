package com.eelection.Home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.eelection.CampaignDataClass
import com.eelection.R
import com.eelection.Videos.VideoDisplayActivity


class UpNextAdapter(private val list: List<CampaignDataClass>, private val context: Context?) :
    RecyclerView.Adapter<UpNextAdapter.MyHoder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoder {

        val view = LayoutInflater.from(context).inflate(R.layout.up_next_layout, parent, false)
        return MyHoder(view)
    }

    override fun onBindViewHolder(holder: MyHoder, @SuppressLint("RecyclerView") position: Int) {
        val campaign = list[position]

        holder.card.setOnClickListener {

            if(campaign.Type.equals("Video")){

                val intent = Intent(context, VideoDisplayActivity::class.java)
                intent.putExtra("CampaignNumber", campaign.CampaignNumber)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context?.startActivity(intent)
            }
        }

        if (context != null) {
            Glide.with(context).load(campaign.Image).into(holder.image)
        }
        holder.title.text=campaign.Title
        holder.partyname.text=campaign.Party_Name
        holder.time.text=campaign.Time

    }

    override fun getItemCount(): Int {

        var arr = 0

        try {
            if (list.size == 0) {

                arr = 0

            } else {

                arr = list.size
            }


        } catch (ignored: Exception) {


        }

        return arr

    }

    inner class MyHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var image: ImageView
        var time: TextView
        var partyname: TextView
        var card: LinearLayout

        init {
            title = itemView.findViewById(R.id.title)
            image = itemView.findViewById(R.id.image)
            card = itemView.findViewById(R.id.card)
            time = itemView.findViewById(R.id.time)
            partyname = itemView.findViewById(R.id.partyname)

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
