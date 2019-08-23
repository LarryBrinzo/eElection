package com.eelection.Home.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.eelection.R


class AllCategoriesAdapter(private val list: List<Pair<String, String>>, private val context: Context?) :
    RecyclerView.Adapter<AllCategoriesAdapter.MyHoder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoder {

        val view = LayoutInflater.from(context).inflate(R.layout.categories_layout, parent, false)
        return MyHoder(view)
    }

    override fun onBindViewHolder(holder: MyHoder, @SuppressLint("RecyclerView") position: Int) {

        holder.text.text = list[position].first

        if (context != null) {
            Glide.with(context).load(getImage(list[position].second)).into(holder.image1)
        }

        holder.card.setOnClickListener {
//            val intent = Intent(context, SearchShowActivity::class.java)
//            intent.putExtra("searchstring", list[position].first)
//            context.startActivity(intent)
        }


    }

    fun getImage(imageName: String): Int {

        return context!!.resources.getIdentifier(imageName, "drawable", context.packageName)
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
        var text: TextView
        var image1: ImageView
        var card: LinearLayout

        init {
            text = itemView.findViewById(R.id.text)
            image1 = itemView.findViewById(R.id.image)
            card = itemView.findViewById(R.id.card)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

