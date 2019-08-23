package com.eelection

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.firebase.database.*

class VideoActivity : AppCompatActivity() {

    var playerView: SimpleExoPlayerView?=null
    var player: ExoPlayer?=null
    var playbackPosition: Long=0
    var playWhenReady: Boolean=true
    var currentWindow=0
    var post:PostDataClass?=null
    lateinit var postNumber:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        playerView=findViewById(R.id.video_view)

        val bundle = intent.extras
        if (bundle != null) {
            postNumber = bundle.getString("PostNumber")
            campaignDetails()
        }
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
        val mediaSource = buildMediaSource(Uri.parse(post?.Video_Url))
        player?.prepare(mediaSource, true, false)

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
                DefaultHttpDataSourceFactory("ua")
            )
            val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
            return DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)
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
        val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Posts").child(postNumber)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                post = dataSnapshot.getValue<PostDataClass>(PostDataClass::class.java)
                post!!.PostNumber=dataSnapshot.key.toString()

                initializePlayer()

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
            postNumber = bundle.getString("PostNumber")
            campaignDetails()
        }

    }

    override fun onStart() {
        super.onStart()

        val bundle = intent.extras
        if (bundle != null) {
            postNumber = bundle.getString("PostNumber")
            campaignDetails()
        }

    }

    override fun onPause() {
        super.onPause()
        releasePlayer()

    }

    override fun onStop() {
        super.onStop()
        releasePlayer()

    }
}
