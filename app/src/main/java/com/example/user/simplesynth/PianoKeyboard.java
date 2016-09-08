package com.example.user.simplesynth;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;

/**
 * Created by user on 05/09/2016.
 */
public class PianoKeyboard extends View {

    Context mContext;
    double mFreq;
    int mAmp;
    int mNoteNum;
    float mX, mY;

    public PianoKeyboard(Context c, AttributeSet attrs) {
        super(c, attrs);
        mContext = c;
        mFreq = 440.f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                noteOn(x, y);
                break;
            case MotionEvent.ACTION_UP:
                noteOff();
                break;
            case MotionEvent.ACTION_MOVE:
                portamento(x, y);
                break;
        }
        return true;
    }

    private void portamento(float x, float y) {
        mX = x;
        mY = y;
    }

    private void noteOn(float x, float y) {
        mX = x;
        mY = y;
    }

    private void noteOff() {
        mY = 0;
    }

    public double freq(){
        float pitch;
        pitch = (mX / getWidth());
        mNoteNum = (int) (pitch * 25.0 + 72);

        double power = (mNoteNum - 69) / 12.0;
        double frequency = Math.pow(2, power) * 440;
        return frequency;
    }

    public short amp(){
        short amp = (short) (mY / getHeight() * 36000);
        return amp;
    }


}
