package com.eelection.Home.Fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.eelection.CampaignDataClass
import com.eelection.Home.Adapters.AllCategoriesAdapter
import com.eelection.Home.Location.GetUsersLocationActivity
import com.eelection.Home.Adapters.PartyCatagoryAdapter
import com.eelection.Home.Adapters.PostAdapter
import com.eelection.PostDataClass
import com.eelection.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.ArrayList
import java.util.HashMap

class HomeFragment : Fragment() {

    private var mDemoSlider: SliderLayout? = null
    internal var url_maps = HashMap<String, Int>()
    var catlist: MutableList<Pair<String, String>> = ArrayList()
    var bjplist: MutableList<CampaignDataClass> = ArrayList()
    var inclist: MutableList<CampaignDataClass> = ArrayList()
    var bsplist: MutableList<CampaignDataClass> = ArrayList()
    var likelist: MutableList<String> = ArrayList()
    var cpilist: MutableList<CampaignDataClass> = ArrayList()
    var postlist: MutableList<PostDataClass> = ArrayList()
    lateinit var uadd : TextView
    lateinit var catrecycl : RecyclerView
    lateinit var bjprecycle : RecyclerView
    lateinit var postrecycle : RecyclerView
    lateinit var increcycle : RecyclerView
    lateinit var bsprecycle : RecyclerView
    lateinit var cpirecycle : RecyclerView
    lateinit var allCategoriesAdapter : AllCategoriesAdapter
    lateinit var bjpAdapter : PartyCatagoryAdapter
    lateinit var postAdapter : PostAdapter
    lateinit var incAdapter : PartyCatagoryAdapter
    lateinit var bspAdapter : PartyCatagoryAdapter
    lateinit var cpiAdapter : PartyCatagoryAdapter
    lateinit var location : LinearLayout

    companion object {

        var useradd : String="0"

        fun newInstance(): HomeFragment {
            val fragmentHome = HomeFragment()
            val args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater!!.inflate(R.layout.fragment_home, container, false)

        mDemoSlider=view.findViewById(R.id.slider)
        catrecycl = view.findViewById(R.id.categoryrecycle)
        bjprecycle = view.findViewById(R.id.bjprecycle)
        increcycle = view.findViewById(R.id.congrecycle)
        bsprecycle = view.findViewById(R.id.bsprecycle)
        cpirecycle = view.findViewById(R.id.cpirecycle)
        postrecycle = view.findViewById(R.id.postrecycle)
        location = view.findViewById(R.id.location)
        uadd= view.findViewById(R.id.uadd)

        postrecycle.setNestedScrollingEnabled(false)

        location.setOnClickListener {
            val intent = Intent(context, GetUsersLocationActivity::class.java)
            startActivity(intent)
        }

        userLikes()
        ImagesfromDatabase()
        recyclerSet()

        return view
    }

    fun ImagesfromDatabase() {

        url_maps.put("1", R.drawable.pic3)
        url_maps.put("2", R.drawable.pic1)
        url_maps.put("3", R.drawable.pic4)
        url_maps.put("4", R.drawable.pic2)

        setUpSliderLayout()
    }

    fun recyclerSet() {

        allCategoriesAdapter = AllCategoriesAdapter(catlist, context)
        val recycecat = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        catrecycl.setLayoutManager(recycecat)
        catrecycl.setLayoutManager(recycecat)
        catrecycl.setAdapter(allCategoriesAdapter)

        addCategories()

        bjpAdapter = PartyCatagoryAdapter(bjplist, context)
        val recycebjb = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bjprecycle.setLayoutManager(recycebjb)
        bjprecycle .setAdapter(bjpAdapter)

        incAdapter = PartyCatagoryAdapter(inclist, context)
        val recyceinc = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        increcycle.setLayoutManager(recyceinc)
        increcycle .setAdapter(incAdapter)

        bspAdapter = PartyCatagoryAdapter(bsplist, context)
        val recycebsp = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bsprecycle.setLayoutManager(recycebsp)
        bsprecycle .setAdapter(bspAdapter)

        cpiAdapter = PartyCatagoryAdapter(cpilist, context)
        val recycecpi = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        cpirecycle.setLayoutManager(recycecpi)
        cpirecycle .setAdapter(cpiAdapter)

        postAdapter = PostAdapter(postlist, context)
        val recycepost = GridLayoutManager(context, 1)
        postrecycle.setLayoutManager(recycepost)
        recycepost.isAutoMeasureEnabled = false
        postrecycle.setItemAnimator(DefaultItemAnimator())
        postrecycle .setAdapter(postAdapter)

        listCampaigns("BJP")
    }

    fun addCategories() {

        catlist.add(Pair("Bharatiya Janata Party", "bjp"))
        catlist.add(Pair("Indian National Congress", "inc"))
        catlist.add(Pair("Bahujan Samaj Party", "bsp"))
        catlist.add(Pair("Communist Party of India", "cpi"))

        allCategoriesAdapter.notifyDataSetChanged()

    }


     fun listCampaigns(party: String) {

        val databaseReference : DatabaseReference  = FirebaseDatabase.getInstance().reference.child("Campaigns")
         val query : Query = databaseReference.orderByChild("Party_Name").equalTo(party)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                bjplist.clear()
                inclist.clear()
                bsplist.clear()
                cpilist.clear()

                for (dataSnapshot1 in dataSnapshot.children) {

                    val campaigntype=dataSnapshot1.child("Type").getValue().toString()

                    val campaign=CampaignDataClass( dataSnapshot1.child("Party_Name").getValue().toString(),
                        if(campaigntype.equals("Video"))  dataSnapshot1.child("Video_Url").getValue().toString() else " ",
                        dataSnapshot1.child("Image").getValue().toString(),
                        campaigntype,
                        dataSnapshot1.child("Title").getValue().toString(),
                        dataSnapshot1!!.key.toString(),
                        dataSnapshot1.child("Time").getValue().toString())

                    when(campaign.Party_Name){
                        "BJP" -> {
                            bjplist.add(campaign)
                            bjpAdapter.notifyDataSetChanged()
                        }
                        "INC" -> {
                            inclist.add(campaign)
                            incAdapter.notifyDataSetChanged()
                        }
                        "BSP" -> {
                            bsplist.add(campaign)
                            bspAdapter.notifyDataSetChanged()
                        }
                        "CPI" -> {
                            cpilist.add(campaign)
                            cpiAdapter.notifyDataSetChanged()
                        }

                    }
                }

                when(party){
                    "BJP" -> {
                        listCampaigns("INC")
                    }
                    "INC" -> {
                        listCampaigns("BSP")
                    }
                    "BSP" -> {
                        listCampaigns("CPI")
                    }
                    "CPI" -> {
                        partyPost()
                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }


    fun partyPost() {

        val databaseReference : DatabaseReference  = FirebaseDatabase.getInstance().reference.child("Posts")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                postlist.clear()

                for (dataSnapshot1 in dataSnapshot.children) {

                    val post : PostDataClass? = dataSnapshot1.getValue<PostDataClass>(PostDataClass::class.java)
                    post!!.PostNumber=dataSnapshot1.key.toString()

                    if(likelist.contains(post.PostNumber))
                        post.UserLike="1"
                    else
                        post.UserLike="0"

                    postlist.add(post)
                    postAdapter.notifyDataSetChanged()

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }


    private fun setUpSliderLayout() {

        for (name in url_maps.keys) {

            val defaultSliderView = DefaultSliderView(context)

            defaultSliderView
                .image(url_maps[name]!!).scaleType = BaseSliderView.ScaleType.Fit

            mDemoSlider!!.addSlider(defaultSliderView)
        }
        mDemoSlider!!.setPresetTransformer(SliderLayout.Transformer.Default)
        mDemoSlider!!.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        mDemoSlider!!.setDuration(6000)
        mDemoSlider!!.stopAutoCycle()

    }

    fun userLikes() {

        val fuser: FirebaseUser? =FirebaseAuth.getInstance().getCurrentUser()
        val userid: String=fuser!!.uid

        val databaseReference :DatabaseReference= FirebaseDatabase.getInstance().reference.child("Users").child(userid).child("Likes")

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    likelist.clear()
                    for (dataSnapshot1 in dataSnapshot.children) {
                        likelist.add(dataSnapshot1.getValue().toString())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

    }


    override fun onResume() {
        super.onResume()
        mDemoSlider!!.startAutoCycle()
    }

    override fun onStart() {
        super.onStart()
        val pref = context!!.getSharedPreferences("MyPref", MODE_PRIVATE)

        useradd = pref.getString("useraddress", null)

        uadd.setText(useradd)
    }

    override fun onPause() {
        super.onPause()
        mDemoSlider!!.stopAutoCycle()
    }

    override fun onStop() {
        super.onStop()
        mDemoSlider!!.stopAutoCycle()
    }


}