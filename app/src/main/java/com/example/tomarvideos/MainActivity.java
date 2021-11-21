package com.example.tomarvideos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Spliterator;

public class MainActivity extends AppCompatActivity {

    private static final int TOMAR_VIDEO = 1;
    private VideoView vv1;
    private Spinner sp1;
    private String[] lista;
    EditText txtnombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vv1=findViewById(R.id.videoView);
        sp1=findViewById(R.id.spinner);
        txtnombre=findViewById(R.id.txtNombre);
        lista=fileList();//lista de archivos de la memoria interna

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,lista);
        sp1.setAdapter(adapter);

    }


    public  void tomarvideo (View v)
    {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,TOMAR_VIDEO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==TOMAR_VIDEO && resultCode==RESULT_OK){
            Uri videoUri=data.getData();
            vv1.setVideoURI(videoUri);
            vv1.start();

            try {
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream in = videoAsset.createInputStream();
                FileOutputStream archivo = openFileOutput(crearNombreArchivoMP4(), MODE_PRIVATE);
                byte[] buf = new byte[1024];
                int len;

                while((len = in.read(buf))>0){
                    archivo.write(buf, 0, len);
                }

            } catch (IOException e) {
                Toast.makeText(this, "Problemas en la grabacion", Toast.LENGTH_SHORT).show();
            }

           
        }


    }//fin onActivityResult

    private String crearNombreArchivoMP4() {

        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre = txtnombre.getText()+" " + fecha + "mp4";
        return nombre;

    }//fin crearNombreArchivoMP4


    public  void ver (View v)
    {
        int pos=sp1.getSelectedItemPosition();
        vv1.setVideoPath(getFilesDir()+"/"+lista[pos]);
        vv1.start();
    }

}