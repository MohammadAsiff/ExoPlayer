package com.example.sys.exoplayer;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    TextView a,b;
    String videoURI,des,title;
    List<JobListItem> jobListItems2;
    Context context;
    private RecyclerView recyclerView2;
    //String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        a=findViewById( R.id.des );
        b=findViewById( R.id.title );
        exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exo_player_view);
        videoURI = getIntent().getStringExtra("url");
        des=getIntent().getStringExtra( "description" );
        title=getIntent().getStringExtra( "title" );
        a.setText( des );
        b.setText( title );
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(Main2Activity.this));
        jobListItems2 = new ArrayList<>();
        ArrayList<JobListItem> jobListItems2 = new ArrayList<JobListItem>();
        jobListItems2 = (ArrayList<JobListItem>) getIntent().getSerializableExtra("joblist");

        //  desc=getIntent().getStringExtra("des");
        try {


            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter ));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource( Uri.parse(videoURI), dataSourceFactory, extractorsFactory, null, null);

            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);

        }catch (Exception e){
            Log.e("MainAcvtivity"," exoplayer error "+ e.toString());
        }
        try {
               /* JSONObject object= new JSONObject( s );
                JSONArray array = object.getJSONArray("contacts");*/
            JSONArray array=new JSONArray( );

            for (int i=0;i<array.length();i++)
            {
                JobListItem item=new JobListItem();
                JSONObject list_obj = array.getJSONObject(i);
                item.setDescription( list_obj.getString("description") );
                item.setThumb(list_obj.getString("thumb"));
                item.setTitle(list_obj.getString("title"));
                item.setUrl(list_obj.getString("url"));
                jobListItems2.add( item );
            }
            recyclerView2.setAdapter( new StudentAdapter( Main2Activity.this,jobListItems2 ) );
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
