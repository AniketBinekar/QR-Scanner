package com.example.qrscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity{
    EditText e1;
    Button b1,b2;
    ImageView i1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1=(EditText)findViewById(R.id.email);
        b1=(Button) findViewById(R.id.btn);
        b2=(Button) findViewById(R.id.btn2);
        i1=(ImageView)findViewById(R.id.qrcode);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=e1.getText().toString();

                if (email!=null && !email.isEmpty())
                {
                    try {
                        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
                        BitMatrix bitMatrix=multiFormatWriter.encode(email,BarcodeFormat.QR_CODE,500,500);
                        BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                        Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                        i1.setImageBitmap(bitmap);
                    }catch (WriterException e)
                    {
                        e.printStackTrace();
                    }
                }
//                WindowManager windowManager=(WindowManager)getSystemService(WINDOW_SERVICE);
//                Display display=windowManager.getDefaultDisplay();
//                Point point=new Point();
//                display.getSize(point);
//                int x=point.x;
//                int y=point.y;
//                int icon=x < y ? x : y;
//                icon=icon *3/4;
//
//                QRCodeEncoder qrCodeEncoder=new QRCodeEncoder(email,null,Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),icon);
//                try {
//                    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
//                    i1.setImageBitmap(bitmap);
//                }catch (WriterException e)
//                {
//                    e.printStackTrace();
//                }


            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator=new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        final IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result!=null && result.getContents()!=null)
        {
            new  AlertDialog.Builder(MainActivity.this)
            .setTitle("Scan Result")
                    .setMessage(result.getContents())
                    .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClipboardManager manager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                            ClipData clipData=ClipData.newPlainText("result",result.getContents());
                            manager.setPrimaryClip(clipData);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}