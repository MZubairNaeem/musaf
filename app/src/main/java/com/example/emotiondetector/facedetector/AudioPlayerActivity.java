package com.example.emotiondetector.facedetector;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.emotiondetector.R;

import java.util.ArrayList;


public class AudioPlayerActivity extends AppCompatActivity {

    private ImageButton pausebtn, playbtn;

    private TextView songName, startTime, songTime;
    private SeekBar songPrgs;

    private ProgressBar loadingProgress;
    private Boolean isPause = false;
    private int pausePosition = 0;

    private MediaPlayer mp;
    private SeekBar seekBar;
    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar;
    private String currentEmotion;
    private String translation;
    private ArrayList<Integer> playlist;
    private int currentItem;

    private Boolean shouldShowProgress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        getIntentData();
        setMoodName();
        bindViews();
        startPlaying();
        listeners();


    }

    private void listeners() {
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPlaying();
            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausePlayer();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                startTime.setText((milliSecondsToTimer(mp.getCurrentPosition())));
                if (fromUser) {
                    if (mp.isPlaying()) {
                        mp.seekTo(progress);
                    }


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void bindViews() {
        seekBar = (SeekBar) findViewById(R.id.sBar);
        loadingProgress = (ProgressBar) findViewById(R.id.loadingProgress);

        playbtn = (ImageButton) findViewById(R.id.btnPlay);
        pausebtn = (ImageButton) findViewById(R.id.btnPause);

        startTime = (TextView) findViewById(R.id.txtStartTime);
        songTime = (TextView) findViewById(R.id.txtSongTime);


        songPrgs = (SeekBar) findViewById(R.id.sBar);
    }


    private void getIntentData() {

        currentEmotion = getIntent().getStringExtra("currentEmotion");
        playlist = new ArrayList<>();
        currentItem = 0;
        if (currentEmotion.equals("angry")) {
            playlist.add(R.raw.angry_1);
            playlist.add(R.raw.angry_2);
            playlist.add(R.raw.angry_3);
        } else if (currentEmotion.equals("fear")) {
            playlist.add(R.raw.fear_1);
            playlist.add(R.raw.fear_2);
        } else if (currentEmotion.equals("happy")) {
            playlist.add(R.raw.happy_1);
            playlist.add(R.raw.happy_2);
            playlist.add(R.raw.happy_3);
        } else if (currentEmotion.equals("sad")) {
            playlist.add(R.raw.sad_1);
            playlist.add(R.raw.sad_2);
            playlist.add(R.raw.sad_3);
        } else {
            playlist.add(R.raw.calm_1);
            playlist.add(R.raw.calm_2);
            playlist.add(R.raw.calm_3);
        }
        if (currentEmotion.equals("angry")) {
            translation = "(O Allah, forgive my sin, and turn away the anger of my heart.) \n (I seek refuge in God from Satan) ";
        } else if (currentEmotion.equals("fear")) {
            translation = "(Ya Allah! I seek Your refuge from worry and sorrow, and I seek Your refuge from humility and laziness, and I seek Your refuge from cowardice and avarice, and I seek Your refuge from the predominance of debt and the oppression of people.) \n" +
                    "Ref: (ابو داوؤد ) ";

        } else if (currentEmotion.equals("happy")) {
            translation = "(Allah I beseech you from the loss of your grace and the transformation of your health." +
                    "And your wrath and all your wrath)";

        } else if (currentEmotion.equals("sad")) {
            translation = "(O Allah, I hope for Your mercy. Do not leave me to myself even for a blink of an eye. Correct all of my affairs for me. There is none worthy of worship except You)";

        } else {
            translation = "(My Lord, aid me and do not aid against me, and\n" +
                    "grant me victory and do not grant victory\n" +
                    "over me, plan for me and do not plan against\n" +
                    "me, guide me and facilitate guidance for me, grant me victory over those who transgress against me.) ";

        }
    }

    private void setMoodName() {
        songName = (TextView) findViewById(R.id.txtSname);
        songName.setText("Ayats for " + currentEmotion + " mood \n" + translation);
    }


    private void pausePlayer() {
        if (mp.isPlaying()) {

            mp.pause();
            isPause = true;
            pausePosition = mp.getCurrentPosition();
            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
            playbtn.setVisibility(View.VISIBLE);
            pausebtn.setVisibility(View.GONE);
        }
    }

    private void startPlaying() {

        if (isPause) {


            mp.seekTo(pausePosition);
            mp.start();
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
            playbtn.setVisibility(View.GONE);
            pausebtn.setVisibility(View.VISIBLE);


        } else {
            loadingProgress.setVisibility(View.VISIBLE);
            play();
            checkCompletion();

        }

    }

    private void play() {
        mp = MediaPlayer.create(AudioPlayerActivity.this, playlist.get(currentItem));
        mp.start();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                bindBarAndMedia();
                setSeekBarData();
            }
        });
    }

    private void checkCompletion() {
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                currentItem++;
                if (playlist.size() > currentItem) {
                    System.out.println("size is "+playlist.size() +" and current item is "+ currentItem);
                    mp.reset();
                    mp = null;
                    mp = MediaPlayer.create(AudioPlayerActivity.this, playlist.get(currentItem));
                    mp.start();
                    setSeekBarData();
                    checkCompletion();
                }

            }
        });
    }

    private void setSeekBarData() {
        seekBar.setMax(mp.getDuration());
        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
        loadingProgress.setVisibility(View.INVISIBLE);
        playbtn.setVisibility(View.GONE);
        pausebtn.setVisibility(View.VISIBLE);
        songTime.setText(milliSecondsToTimer(mp.getDuration()));
    }


    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

// Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
// Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

// Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

// return timer string
        return finalTimerString;
    }

    private void bindBarAndMedia() {

        mUpdateSeekbar = new Runnable() {
            @Override
            public void run() {
                if (shouldShowProgress) {
                    seekBar.setProgress(mp.getCurrentPosition());
                    mSeekbarUpdateHandler.postDelayed(this, 0);
                }
            }
        };
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
            shouldShowProgress = false;
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}