package au.gov.defence.royalaustraliannavyfitness;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.VideoView;

public class FullscreenVideoActivity extends Activity {

    private VideoView fullscreenVideoView;
    private SeekBar videoSeekBar;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_video);

        fullscreenVideoView = findViewById(R.id.fullscreenVideoView);

        Intent intent = getIntent();
        Uri videoUri = intent.getData();
        int currentPosition = intent.getIntExtra("currentPosition", 0);

        fullscreenVideoView.setVideoURI(videoUri);
        fullscreenVideoView.seekTo(currentPosition);
        fullscreenVideoView.start();
        videoSeekBar = findViewById(R.id.videoSeekBar);
        fullscreenVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoSeekBar.setMax(fullscreenVideoView.getDuration());
                updateSeekBar();
            }
        });

        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    fullscreenVideoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateRunnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.postDelayed(updateRunnable, 0);
            }
        });
    }
    private void updateSeekBar() {
        videoSeekBar.setProgress(fullscreenVideoView.getCurrentPosition());
        handler.postDelayed(updateRunnable, 1000);
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateRunnable);
    }
}
