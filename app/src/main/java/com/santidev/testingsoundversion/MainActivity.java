package com.santidev.testingsoundversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //sound pool
    SoundPool sp;
    int nowPlaying = -1;
    int idFx1 = -1;
    int idFx2 = -1;
    int idFx3 = -1;

    float volume = .1f;
    int repeats = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFx1 = (Button) findViewById(R.id.btnFx1);
        Button btnFx2 = (Button) findViewById(R.id.btnFx2);
        Button btnFx3 = (Button) findViewById(R.id.btnFx3);
        Button btnStop = (Button) findViewById(R.id.btnStop);

        btnFx1.setOnClickListener(this);
        btnFx2.setOnClickListener(this);
        btnFx3.setOnClickListener(this);
        btnStop.setOnClickListener(this);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //El codigo de lollipop o posterior
            AudioAttributes attr = new AudioAttributes.Builder()
                                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                        .build();
            sp = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(attr)
                    .build();

        }else {
            //el codigo anterior para dispositivos mas viejos
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        AssetManager manager = this.getAssets();
        AssetFileDescriptor descriptor = null;

        try {

            //cargamos en memoria el audio fx1.ogg
            descriptor = manager.openFd("fx1.ogg");
            idFx1 = sp.load(descriptor,0);

            descriptor = manager.openFd("fx2.ogg");
            idFx2 = sp.load(descriptor,0);

            descriptor = manager.openFd("fx3.ogg");
            idFx3 = sp.load(descriptor, 0 );

        } catch (IOException e) {
            e.printStackTrace();
        }

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                volume = value/10.0f;
                sp.setVolume(nowPlaying, volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner) ;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = String.valueOf(spinner.getSelectedItem());
                repeats = Integer.parseInt(temp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //REPRODUCIR SONIDO

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnFx1:
                sp.stop(nowPlaying);
                nowPlaying = sp.play(idFx1, volume,volume,0,repeats,1);
                break;
            case R.id.btnFx2:
                sp.stop(nowPlaying);
                nowPlaying = sp.play(idFx2, volume,volume,0,repeats,1);
                break;
            case R.id.btnFx3:
                sp.stop(nowPlaying);
                nowPlaying = sp.play(idFx3, volume,volume,0,repeats, 1);
                break;
            case R.id.btnStop:
                sp.stop(nowPlaying);
                break;
        }
    }
}