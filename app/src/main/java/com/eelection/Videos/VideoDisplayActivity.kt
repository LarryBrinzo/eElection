package com.eelection.Videos

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.eelection.CampaignDataClass
import com.eelection.Home.UpNextAdapter
import com.eelection.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource.*
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_video_display.*
import java.util.ArrayList

class VideoDisplayActivity : AppCompatActivity() {

    var playerView: SimpleExoPlayerView?=null
    var player: ExoPlayer?=null
    var playbackPosition: Long=0
    var playWhenReady: Boolean=true
    var currentWindow=0
    lateinit var campaignNumber:String
    var campaign:CampaignDataClass?=null
    var title:TextView?=null
    lateinit var container: ShimmerFrameLayout
    var time:TextView?=null
    var partyimg:ImageView?=null
    lateinit var upNextAdapter : UpNextAdapter
    var upNextlist: MutableList<CampaignDataClass> = ArrayList()
    lateinit var upNextRecycle: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_display)

        playerView=findViewById(R.id.video_view)
        title=findViewById(R.id.title)
        time=findViewById(R.id.time)
        partyimg=findViewById(R.id.partyimg)
        upNextRecycle=findViewById(R.id.upnextrecycle)

        upNextRecycle.setNestedScrollingEnabled(false)

        val bundle = intent.extras
        if (bundle != null) {
            campaignNumber = bundle.getString("CampaignNumber")
            campaignDetails()
            recycleSet()
        }
    }

    fun recycleSet(){

        upNextAdapter = UpNextAdapter(upNextlist, applicationContext)
        val recycenext = GridLayoutManager(applicationContext, 1)
        upnextrecycle.setLayoutManager(recycenext)
        recycenext.isAutoMeasureEnabled = false
        upNextRecycle.setItemAnimator(DefaultItemAnimator())
        upNextRecycle .setAdapter(upNextAdapter)

        listCampaigns()

    }

    fun listCampaigns() {

        val databaseReference : DatabaseReference  = FirebaseDatabase.getInstance().reference.child("Campaigns")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                upNextlist.clear()

                for (dataSnapshot1 in dataSnapshot.children) {

                    val campaigntype=dataSnapshot1.child("Type").getValue().toString()

                    val campaign=CampaignDataClass( dataSnapshot1.child("Party_Name").getValue().toString(),
                        if(campaigntype.equals("Video"))  dataSnapshot1.child("Video_Url").getValue().toString() else " ",
                        dataSnapshot1.child("Image").getValue().toString(),
                        campaigntype,
                        dataSnapshot1.child("Title").getValue().toString(),
                        dataSnapshot1!!.key.toString(),
                        dataSnapshot1.child("Time").getValue().toString())

                    if(!campaign.CampaignNumber.equals(campaignNumber)){
                        upNextlist.add(campaign)
                        upNextAdapter.notifyDataSetChanged()
                    }
                }

//                container.visibility = View.GONE
//                container.stopShimmer()

            }

            override fun onCancelled(databaseError: DatabaseError) {
//                container.visibility = View.GONE
//                container.stopShimmer()
            }
        })

    }

    private fun initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(applicationContext),
                DefaultTrackSelector(),
                DefaultLoadControl()
            )
            playerView?.setPlayer(player)
            player!!.setPlayWhenReady(playWhenReady)
            player!!.seekTo(currentWindow, playbackPosition)
        }
        val mediaSource = buildMediaSource(Uri.parse(campaign?.Video_URL))
        player?.prepare(mediaSource, true, false)

        Handler().postDelayed({
            container.visibility = View.GONE
            container.stopShimmer()
        },6000)

    }

    private fun buildMediaSource(uri: Uri): MediaSource {

        val userAgent = "exoplayer-codelab"

        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {
            return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else if (uri.getLastPathSegment().contains("m3u8")) {
            return HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else {
            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(
                DefaultHttpDataSourceFactory("ua"))
            val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
            return Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)
        }

    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.getCurrentPosition()
            currentWindow = player!!.getCurrentWindowIndex()
            playWhenReady = player!!.getPlayWhenReady()
            player!!.release()
            player = null
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUiFullScreen()
        } else {
            hideSystemUi()
        }
    }

    @SuppressLint("InlinedApi")
    fun hideSystemUiFullScreen() {
        playerView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    fun campaignDetails(){
        val databaseReference : DatabaseReference=FirebaseDatabase.getInstance().reference.child("Campaigns").child(campaignNumber)

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val campaigntype=dataSnapshot.child("Type").getValue().toString()

                        campaign=CampaignDataClass( dataSnapshot.child("Party_Name").getValue().toString(),
                            if(campaigntype.equals("Video"))  dataSnapshot.child("Video_Url").getValue().toString() else " ",
                            dataSnapshot.child("Image").getValue().toString(),
                            campaigntype,
                            dataSnapshot.child("Title").getValue().toString(),
                            dataSnapshot!!.key.toString(),
                            dataSnapshot.child("Time").getValue().toString())

                    initializePlayer()

                        title!!.text= campaign!!.Title
                        time!!.text= campaign!!.Time

                        when(campaign!!.Party_Name){
                            "BJP" -> partyimg?.let { Glide.with(applicationContext).load(getImage("bjp")).into(it) }
                            "INC" -> partyimg?.let { Glide.with(applicationContext).load(getImage("inc")).into(it) }
                            "BSP" -> partyimg?.let { Glide.with(applicationContext).load(getImage("bsp")).into(it) }
                            "CPI" -> partyimg?.let { Glide.with(applicationContext).load(getImage("cpi")).into(it) }
                        }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

    }

    fun getImage(imageName: String): Int {

        return applicationContext!!.resources.getIdentifier(imageName, "drawable", applicationContext.packageName)
    }

    override fun onResume() {
        super.onResume()

        val bundle = intent.extras
        if (bundle != null) {
            campaignNumber = bundle.getString("CampaignNumber")
            campaignDetails()
        }

    }

    override fun onStart() {
        super.onStart()

        container =findViewById(R.id.shimmer_view_container)
        container.startShimmer()

        val bundle = intent.extras
        if (bundle != null) {
            campaignNumber = bundle.getString("CampaignNumber")
            campaignDetails()
        }

    }

    override fun onPause() {
        super.onPause()
        releasePlayer()

        container.visibility = View.GONE
        container.stopShimmer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()

        container.visibility = View.GONE
        container.stopShimmer()
    }

}
