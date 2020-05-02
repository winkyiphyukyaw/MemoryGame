package com.iss.team4.ca2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Activity2 extends AppCompatActivity {
    Intent in ;

    TextView tv_p1, tv_p2;

    ImageView img1, iv_12, iv_13, iv_14, iv_21, iv_22, iv_23, iv_24, iv_31, iv_32, iv_33, iv_34;

    //array for the images
    Integer[] cardsArray = {101,102,103,104,105,106,201,202,203,204,205,206};

    //actual images
    int image101, image102, image103, image104, image105,image106,
            image201, image202, image203, image204, image205,image206;

    int firstCard, secondCard;
    int clickedFirst, clickedSecond;
    int cardNumber = 1;

    int turn = 1;
    int playerPoints = 0, cpuPoints = 0;

    private Chronometer mChronometer;

    static long startTime;
    static long endTime;
    static long runTime;

    SoundPool soundPool;
    HashMap<Integer, Integer> musicId=new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        in = new Intent( this,MainActivity.class);

        ArrayList<Integer> PositionSelected = getIntent().getIntegerArrayListExtra("Selected_imgId");
        ArrayList<String> imgUrl = getIntent().getStringArrayListExtra("Img_URLs");

        for (int i = 0; i < PositionSelected.size(); i++) {
            long imageLen = 0;
            long totalSoFar = 0;
            int readLen = 0;
            Bitmap bitmap = null;
            String tempurl = imgUrl.get(PositionSelected.get(i));
            // System.out.println("Here I am lost" + tempurl);
            String saveToPath = getFilesDir() + "/" + "Image" + i + ".jpg";
            try {
                URL url = new URL(tempurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                imageLen = conn.getContentLength();
                byte[] data = new byte[1024];

                //System.out.println("Here I am lost 2" + saveToPath);

                InputStream in = url.openStream();
                BufferedInputStream bufIn = new BufferedInputStream(in, 2048);
                OutputStream out = new FileOutputStream(saveToPath);

                while ((readLen = bufIn.read(data)) != -1) {
                    totalSoFar += readLen;
                    out.write(data, 0, readLen);
                }
                System.out.println("Here I am lost again ****" + saveToPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.start();

        startTime = System.currentTimeMillis();

        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC,0);
        musicId.put(1, soundPool.load(this, R.raw.dodo, 1));
        musicId.put(2,soundPool.load(this, R.raw.ding, 1));

        Intent playmusic = new Intent(Activity2.this,Music.class);
        startService(playmusic);

        tv_p1 = (TextView) findViewById(R.id.tv_p1);
        tv_p2 = (TextView) findViewById(R.id.tv_p2);

        img1 = (ImageView) findViewById(R.id.img1);
        iv_12 = (ImageView) findViewById(R.id.img2);
        iv_13 = (ImageView) findViewById(R.id.img3);
        iv_14 = (ImageView) findViewById(R.id.img4);
        iv_21 = (ImageView) findViewById(R.id.img5);
        iv_22 = (ImageView) findViewById(R.id.img6);
        iv_23 = (ImageView) findViewById(R.id.img7);
        iv_24 = (ImageView) findViewById(R.id.img8);
        iv_31 = (ImageView) findViewById(R.id.img9);
        iv_32 = (ImageView) findViewById(R.id.img10);
        iv_33 = (ImageView) findViewById(R.id.img11);
        iv_34 = (ImageView) findViewById(R.id.img12);

        img1.setTag("0");
        iv_12.setTag("1");
        iv_13.setTag("2");
        iv_14.setTag("3");
        iv_21.setTag("4");
        iv_22.setTag("5");
        iv_23.setTag("6");
        iv_24.setTag("7");
        iv_31.setTag("8");
        iv_32.setTag("9");
        iv_33.setTag("10");
        iv_34.setTag("11");

        //load the card images
        frontOfCardsResources();

        //shuffle the images
        Collections.shuffle(Arrays.asList(cardsArray));

        //changing the color of the second player (inactive)
        tv_p2.setTextColor(Color.GRAY);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(img1, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });

        iv_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_12, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });

        iv_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_13, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });

        iv_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_14, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });
        iv_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_21, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });

        iv_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_22, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });

        iv_23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_23, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });

        iv_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_24, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });
        iv_31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_31, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });

        iv_32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_32, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });

        iv_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_33, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });

        iv_34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(iv_34, theCard);
                soundPool.play(musicId.get(1),1,1,0,0,1);
            }
        });

    }

    private void doStuff(ImageView iv, int card){
        //set the correct image to the imageview
        if(cardsArray[card] == 101){
            iv.setImageResource(image101);
        }
        else if(cardsArray[card] ==102){
            iv.setImageResource(image102);
        }
        else if(cardsArray[card] ==103){
            iv.setImageResource(image103);
        }
        else if(cardsArray[card] ==104){
            iv.setImageResource(image104);
        }
        else if (cardsArray[card] == 105){
            iv.setImageResource(image105);
        }
        else if(cardsArray[card] ==106){
            iv.setImageResource(image106);
        }
        else if(cardsArray[card] ==201){
            iv.setImageResource(image201);
        }
        else if(cardsArray[card] ==202){
            iv.setImageResource(image202);
        }
        else if (cardsArray[card] == 203){
            iv.setImageResource(image203);
        }
        else if(cardsArray[card] ==204){
            iv.setImageResource(image204);
        }
        else if(cardsArray[card] ==205){
            iv.setImageResource(image205);
        }
        else if(cardsArray[card] ==206){
            iv.setImageResource(image206);
        }

        //check which image is selected and save it to temporary variable
        if (cardNumber == 1) {
            firstCard = cardsArray[card];
            if(firstCard > 200){
                firstCard = firstCard - 100;
            }
            cardNumber = 2;
            clickedFirst=card;


            iv.setEnabled(false);
        } else if(cardNumber==2){
            secondCard = cardsArray[card];
            if(secondCard > 200){
                secondCard = secondCard - 100;
            }
            cardNumber = 1;
            clickedSecond = card;

            img1.setEnabled(false);
            iv_12.setEnabled(false);
            iv_13.setEnabled(false);
            iv_14.setEnabled(false);
            iv_21.setEnabled(false);
            iv_22.setEnabled(false);
            iv_23.setEnabled(false);
            iv_24.setEnabled(false);
            iv_31.setEnabled(false);
            iv_32.setEnabled(false);
            iv_33.setEnabled(false);
            iv_34.setEnabled(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //check if the selected images are equal
                    calculate();
                }
            },1000 );
        }
    }

    private void calculate() {
        //if images are equal remove them and add points

        if (firstCard == secondCard) {soundPool.play(musicId.get(2),1,1,0,0,1);
            if (clickedFirst == 0) {
                img1.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 1) {
                iv_12.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 2) {
                iv_13.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 3) {
                iv_14.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 4) {
                iv_21.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 5) {
                iv_22.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 6) {
                iv_23.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 7) {
                iv_24.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 8) {
                iv_31.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 9) {
                iv_32.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 10) {
                iv_33.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 11) {
                iv_34.setVisibility(View.INVISIBLE);
            }

            if (clickedSecond == 0) {
                img1.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 1) {
                iv_12.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 2) {
                iv_13.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 3) {
                iv_14.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 4) {
                iv_21.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 5) {
                iv_22.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 6) {
                iv_23.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 7) {
                iv_24.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 8) {
                iv_31.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 9) {
                iv_32.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 10) {
                iv_33.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 11) {
                iv_34.setVisibility(View.INVISIBLE);
            }

            //add points to the correct player
            if (turn == 1) {
                playerPoints++;
                tv_p1.setText("P1: " + playerPoints);
            } else if (turn == 2) {
                cpuPoints++;
                tv_p2.setText("P2: " + cpuPoints);
            }
        } else {
            img1.setImageResource(R.drawable.ic_back);
            iv_12.setImageResource(R.drawable.ic_back);
            iv_13.setImageResource(R.drawable.ic_back);
            iv_14.setImageResource(R.drawable.ic_back);
            iv_21.setImageResource(R.drawable.ic_back);
            iv_22.setImageResource(R.drawable.ic_back);
            iv_23.setImageResource(R.drawable.ic_back);
            iv_24.setImageResource(R.drawable.ic_back);
            iv_31.setImageResource(R.drawable.ic_back);
            iv_32.setImageResource(R.drawable.ic_back);
            iv_33.setImageResource(R.drawable.ic_back);
            iv_34.setImageResource(R.drawable.ic_back);


            //change player turn
            if (turn == 1) {
                turn = 2;
                tv_p1.setTextColor(Color.GRAY);
                tv_p2.setTextColor(Color.BLACK);
            } else if
            (turn == 2) {
                turn = 1;
                tv_p2.setTextColor(Color.GRAY);
                tv_p1.setTextColor(Color.BLACK);
            }
        }
        img1.setEnabled(true);
        iv_12.setEnabled(true);
        iv_13.setEnabled(true);
        iv_14.setEnabled(true);
        iv_21.setEnabled(true);
        iv_22.setEnabled(true);
        iv_23.setEnabled(true);
        iv_24.setEnabled(true);
        iv_31.setEnabled(true);
        iv_32.setEnabled(true);
        iv_33.setEnabled(true);
        iv_34.setEnabled(true);

        //to check if game is over
        checkEnd();
    }


    private void checkEnd() {
        if (img1.getVisibility() == View.INVISIBLE &&
                iv_12.getVisibility() == View.INVISIBLE &&
                iv_13.getVisibility() == View.INVISIBLE &&
                iv_14.getVisibility() == View.INVISIBLE &&
                iv_21.getVisibility() == View.INVISIBLE &&
                iv_22.getVisibility() == View.INVISIBLE &&
                iv_23.getVisibility() == View.INVISIBLE &&
                iv_24.getVisibility() == View.INVISIBLE &&
                iv_31.getVisibility() == View.INVISIBLE &&
                iv_32.getVisibility() == View.INVISIBLE &&
                iv_33.getVisibility() == View.INVISIBLE &&
                iv_34.getVisibility() == View.INVISIBLE) {

            endTime = System.currentTimeMillis();
            runTime = (endTime - startTime)/1000;


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity2.this);
            alertDialogBuilder
                    .setTitle("GAME OVER!")
                    .setMessage("\nP1: " + playerPoints + "\nP2: " + cpuPoints +"\nTIME ELAPSED: " + runTime + "seconds")
                    .setCancelable(false)
                    .setPositiveButton("NEW", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getApplicationContext(), Activity2.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    protected void onStop(){
        Intent stopmusic = new Intent(getApplicationContext(),Music.class);
        stopService(stopmusic);
        super.onStop();


        mChronometer.stop();
        long timeend = SystemClock.elapsedRealtime();
    }


    private void frontOfCardsResources(){
//    String saveToPath;
//    File imgFile;
//        for(int i =0; i< 12; i++){
//            saveToPath = getFilesDir()+"/" + "Image" + i + ".jpg";
//            imgFile = new File(saveToPath);
//        }



        String saveToPath1 = getFilesDir() + "/" + "Image" + 0 + ".jpg";

        File imgFile1 = new  File(saveToPath1);


//        if(imgFile1.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());
//            image101 = (ImageView) findViewById(R.id.img1);
//            //image101.setImageBitmap(myBitmap);
//       }
//
//        String saveToPath7 = getFilesDir() + "/" + "Image" + 0 + ".jpg";
//
//        File imgFile7 = new  File(saveToPath7);
//
//        if(imgFile1.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile7.getAbsolutePath());
//            ImageView image201 = (ImageView) findViewById(R.id.img7);
//           // image201.setImageBitmap(myBitmap);
//        }
//
//        String saveToPath2 = getFilesDir() + "/" + "Image" + 1 + ".jpg";
//
//        File imgFile2 = new  File(saveToPath2);
//
//        if(imgFile2.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
//            ImageView image102 = (ImageView) findViewById(R.id.img2);
//            image102.setImageBitmap(myBitmap);
//        }
//
//        String saveToPath8 = getFilesDir() + "/" + "Image" + 1 + ".jpg";
//
//        File imgFile8 = new  File(saveToPath8);
//
//        if(imgFile8.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile8.getAbsolutePath());
//            ImageView image202 = (ImageView) findViewById(R.id.img8);
//            image202.setImageBitmap(myBitmap);
//        }
//
//        String saveToPath3 = getFilesDir() + "/" + "Image" + 2 + ".jpg";
//
//        File imgFile3 = new  File(saveToPath3);
//
//        if(imgFile3.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile3.getAbsolutePath());
//            ImageView image103 = (ImageView) findViewById(R.id.img3);
//            image103.setImageBitmap(myBitmap);
//        }
//
//        String saveToPath9 = getFilesDir() + "/" + "Image" + 2 + ".jpg";
//
//        File imgFile9 = new  File(saveToPath9);
//
//        if(imgFile9.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile9.getAbsolutePath());
//            ImageView image203 = (ImageView) findViewById(R.id.img9);
//            image203.setImageBitmap(myBitmap);
//        }
//
//
//        String saveToPath4 = getFilesDir() + "/" + "Image" + 3 + ".jpg";
//
//        File imgFile4 = new  File(saveToPath4);
//
//        if(imgFile4.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile4.getAbsolutePath());
//            ImageView image104 = (ImageView) findViewById(R.id.img4);
//            image104.setImageBitmap(myBitmap);
//        }
//
//        String saveToPath10 = getFilesDir() + "/" + "Image" + 3 + ".jpg";
//
//        File imgFile10 = new  File(saveToPath10);
//
//        if(imgFile10.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile10.getAbsolutePath());
//            ImageView image204 = (ImageView) findViewById(R.id.img10);
//            image204.setImageBitmap(myBitmap);
//        }
//
//        String saveToPath5 = getFilesDir() + "/" + "Image" + 4 + ".jpg";
//
//        File imgFile5 = new  File(saveToPath5);
//
//        if(imgFile5.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile5.getAbsolutePath());
//            ImageView image105 = (ImageView) findViewById(R.id.img5);
//            image105.setImageBitmap(myBitmap);
//        }
//
//        String saveToPath11 = getFilesDir() + "/" + "Image" + 4 + ".jpg";
//
//        File imgFile11 = new  File(saveToPath11);
//
//        if(imgFile11.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile11.getAbsolutePath());
//            ImageView image205 = (ImageView) findViewById(R.id.img11);
//            image205.setImageBitmap(myBitmap);
//        }
//
//        String saveToPath6 = getFilesDir() + "/" + "Image" + 5 + ".jpg";
//
//        File imgFile6 = new  File(saveToPath6);
//
//        if(imgFile6.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile6.getAbsolutePath());
//            ImageView image106 = (ImageView) findViewById(R.id.img6);
//            image106.setImageBitmap(myBitmap);
//        }
//        String saveToPath12 = getFilesDir() + "/" + "Image" + 5 + ".jpg";
//
//        File imgFile12 = new  File(saveToPath12);
//
//        if(imgFile12.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile12.getAbsolutePath());
//            ImageView image206 = (ImageView) findViewById(R.id.img12);
//            image206.setImageBitmap(myBitmap);
//        }
    }

    public void displayImage(){

       /* for(int i = 0; i<6 ;i++)
        {
            String saveToPath = getFilesDir() + "/" + "Image" + i + ".jpg";
            String place = "R.id.img"+ i;

            File imgFile = new  File(saveToPath);

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                ImageView myImage = (ImageView) findViewById(Integer.parseInt(place));
                System.out.println("check here!!");

                myImage.setImageBitmap(myBitmap);

            }
        }*/



    }
}

