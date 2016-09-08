package com.example.user.simplesynth;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by user on 02/09/2016.
 */
public class MainActivity extends AppCompatActivity {

    Thread t;
    int sr = 44100;
    boolean isRunning = true;
    PianoKeyboard mKeyboard;

    //start a new thread to synthesise audio
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mKeyboard = (PianoKeyboard) findViewById(R.id.keyboard);

        t = new Thread() {
            public void run() {
                setPriority(Thread.MAX_PRIORITY);

                int buffsize = AudioTrack.getMinBufferSize(
                        sr,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT
                );

                // create an AudioTrack object
                AudioTrack audioTrack = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        sr,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        buffsize,
                        AudioTrack.MODE_STREAM
                );

                short samples[] = new short[buffsize];
                double twopi = 8.*Math.atan(1.);
                double ph = 0.0;

                // start audio
                audioTrack.play();

                // synthesis loop
                while(isRunning){

                    double fr = mKeyboard.freq();

                    for(int i=0; i < buffsize; i++){
                        samples[i] = (short) (mKeyboard.amp() * Math.sin(ph));
                        ph += twopi*fr/sr;
                    }
                    audioTrack.write(samples, 0, buffsize);
                }

                audioTrack.stop();
                audioTrack.release();
            }
        };

        t.start();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        isRunning = false;
        try{
            t.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        t = null;
    }
}
