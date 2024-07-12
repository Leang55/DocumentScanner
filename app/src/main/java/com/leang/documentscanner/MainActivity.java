package com.leang.documentscanner;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.leang.documentscanner.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView Clear, getImage, Copy;
    EditText recgText;
    Uri imageUri;
    TextRecognizer textRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Clear = findViewById(R.id.Clear);
        getImage = findViewById(R.id.getImage);
        Copy = findViewById(R.id.Copy);
        recgText = findViewById(R.id.recgText);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(MainActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(this,data.getData().toString(), Toast.LENGTH_LONG).show();
//        if (requestCode == Activity.RESULT_OK) {

            if (data != null) {

                imageUri = data.getData();
                Toast.makeText(this, "images selected", Toast.LENGTH_LONG).show();

                recognizeText();
            }

//        } else {
//
//            Toast.makeText(this, "images not select", Toast.LENGTH_LONG).show();
//        }
    }

    private void recognizeText() {
        if (imageUri != null) {

            try {
                InputImage inputImage = InputImage.fromFilePath(MainActivity.this, imageUri);

                Task<Text> result = textRecognizer.process(inputImage)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text text) {

                                String recognizeText = text.getText();
                                recgText.setText(recognizeText);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });

            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }
}


