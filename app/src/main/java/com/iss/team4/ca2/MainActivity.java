package com.iss.team4.ca2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    EditText input;
    Button fetch;
    GridView GV;
    ProgressBar pg;
    TextView tv;

    ArrayList<String> imgUrl = new ArrayList<>();
    ArrayList<Bitmap> imageBtps = new ArrayList<Bitmap>();
  ArrayList<Integer>  PositionSelected = new ArrayList<Integer>();
  //  HashMap<String,Integer> PositionSelected = new HashMap<String,Integer>()
    static String AUTOFILL_HINT_NAME = "Selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        tv = (TextView) findViewById(R.id.textView);
        input = (EditText) findViewById(R.id.url);
        fetch = (Button) findViewById(R.id.fetch);
        GV = (GridView) findViewById(R.id.gridview);
        pg = (ProgressBar) findViewById(R.id.progressBar);

        pg.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String source = input.getText().toString();
                System.out.println(source);

                try {
                    URL test = new URL(source);
                    URLConnection uc = test.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

                    String inputLine;
                    StringBuilder sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                        System.out.println(inputLine);
                    }
                    in.close();
                    String SourceHTML = sb.toString();

                    imgUrl.clear();
                    GV.invalidateViews();
                    GV.setAdapter(new gridViewAdapter(getBaseContext()));

                    //Read all the image url's into to the Arraylist of "imgUrl"
                    String imgtag = "<img src=\"";
                    int index = SourceHTML.indexOf(imgtag);

                    for (Integer j = 1; j <= 21; j++)
                    {
                        index = index + imgtag.length();
                        System.out.println(index);
                        System.out.println("Here is the URL for the" + "j" + " Image" + SourceHTML.substring(index, SourceHTML.indexOf("\"", index + 1)));
                        //skip  the first image  and save others images
                        if(j!=1){
                            imgUrl.add(SourceHTML.substring(index, SourceHTML.indexOf("\"", index + 1)));
                            //System.out.println("Here is the URL for the" + "j" + " Image" + imgUrl.get(j - 2));
                        }
                        index = SourceHTML.indexOf(imgtag, index + imgtag.length());
                    }
                    pg.setProgress(0);
                    pg.setMax(imgUrl.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

       GV.setAdapter(new gridViewAdapter(this));
        pg.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);

        GV.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            View obj_view;
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                pg.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.INVISIBLE);
                Toast.makeText(getBaseContext(), "Image " + (position + 1) + " Selected", Toast.LENGTH_LONG).show();

                System.out.println(position);

                if(!(PositionSelected.contains(position)))
                {
                    if (PositionSelected.size() <6) {
                        PositionSelected.add(position);
                        //System.out.println(PositionSelected.);
                        View viewPrev = (View) GV.getChildAt(position);
                        GV.getChildAt(position).setAlpha((float) 0.5);
                       // System.out.println("I am lost again "+PositionSelected.indexOf(position));
                    }
                    else {

                        Toast.makeText(getBaseContext(), "-  Maximum of 6 items chosen already.", Toast.LENGTH_LONG).show();

                        for (int i : PositionSelected) {
                            System.out.println("Here is the list !"+i);
                        }

                        Bundle B = new Bundle();
                        B.putIntegerArrayList("Selected_imgId", PositionSelected);
                        B.putStringArrayList("Img_URLs", imgUrl);
                        Intent intent = new Intent(getApplicationContext(), Activity2.class);
                        intent.putExtras(B);
                        startActivity(intent);
                    }
                }
                else {
                            // if user selected the same icon again deselected.
                PositionSelected.remove(PositionSelected.indexOf(position));
                Toast.makeText(getBaseContext(), "Grid Item " + (position + 1) + " Removed", Toast.LENGTH_LONG).show();
                View viewPrev = (View) GV.getChildAt(position);
                GV.getChildAt(position).setAlpha((float) 1);
                    }
            }
        });
    }
    public class gridViewAdapter extends BaseAdapter {
        private Context obj_context;
        //constructor
        public gridViewAdapter(Context c) {
            obj_context = c;
        }
        public gridViewAdapter() {
        }
        @Override
        public int getCount() {
            return imgUrl.size();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imgview;
            if (convertView == null) {
                imgview = new ImageView(obj_context);
                imgview.setLayoutParams(new GridView.LayoutParams(248, 248));
                imgview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imgview.setPadding(8, 8, 8, 8);
                System.out.println(position);
                new DownloadImageTask(imgview, position).execute(imgUrl.get(position) );
            } else {
                imgview = (ImageView) convertView;
            }
            imgview.setImageResource(R.drawable.imageholder);
            return imgview;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView ImageBitmap;
        int pos;

        public DownloadImageTask(ImageView ImageBitmap, int pos) {
            this.ImageBitmap = ImageBitmap;
            this.pos = pos;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bmp = null;
            try {
                URL surl = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection) surl.openConnection() ;
                if(con==null)
                    return null;

                con.setDoInput(true);
                con.connect();

                InputStream in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                //System.out.println("Storing bitmap =>" +this.pos);
                imageBtps.add(bmp);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmp;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            ImageBitmap.setImageBitmap(result);
            System.out.println("Storing bitmap =>" +this.pos);
            pg.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            pg.setProgress(this.pos);
            tv.setText(String.format("Downloading %d of %d images...", this.pos, imgUrl.size()));
            if (this.pos == 0 & imageBtps.size() > 0) {
                pg.setProgress(imgUrl.size());
                tv.setText(String.format("Downloaded %d of %d images...", imgUrl.size(), imgUrl.size()));
            }
        }
    }

}
