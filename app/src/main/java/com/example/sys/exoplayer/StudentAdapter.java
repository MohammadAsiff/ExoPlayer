package com.example.sys.exoplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder>  {
    Context context;
    List<JobListItem> jobList =new ArrayList<>(  );
    public StudentAdapter(Context context, List<JobListItem> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.studentlist, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.description.setText(jobList.get(i).getDescription());
        myViewHolder.title.setText(jobList.get(i).getTitle());
        String thumb=jobList.get(i).getThumb();
        new Download(myViewHolder.imageView).execute(thumb);
        myViewHolder.imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent( context,Main2Activity.class );
                intent.putExtra( "url",jobList.get(i).getUrl() );
                intent.putExtra( "description",jobList.get(i ).getDescription() );
                intent.putExtra( "title",jobList.get( i ).getTitle() );
                intent.putExtra( "joblist",(Serializable)jobList );
                jobList.remove( i );
                context.startActivity(intent );

            }
        } );

    }
    private class Download extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;


        public Download(ImageView imageView) {
            this.imageView=imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String image_url = strings[0];
            StringBuilder sb = new StringBuilder(image_url);

            sb.insert(4, 's');

            String url1=sb.toString();
            Bitmap bitmap = null;
            try {
                InputStream input = new URL(url1 ).openStream();
                bitmap = BitmapFactory.decodeStream( input );
                input.close();
                File storagePath = Environment.getExternalStorageDirectory();
                OutputStream bytes = new FileOutputStream( new File( storagePath, "name.jpg" ) );

                bitmap.compress( Bitmap.CompressFormat.JPEG, 100, bytes );
                bytes.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap( bitmap );
        }
    }
    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView description,title;
        ImageView imageView;

        public MyViewHolder(@NonNull  final View itemView) {
            super( itemView );
            description=itemView.findViewById( R.id.des );
            title=itemView.findViewById( R.id.title );
            imageView=itemView.findViewById( R.id.thumb );


        }
    }
}