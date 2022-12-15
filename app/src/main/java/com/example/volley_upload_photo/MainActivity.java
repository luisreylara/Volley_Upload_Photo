package com.example.volley_upload_photo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// https://www.youtube.com/watch?v=YMUGyIjV8Yw
public class MainActivity extends AppCompatActivity {
private ImageButton ib1;
private Button button1;
Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ib1= findViewById(R.id.ib1);
        button1=findViewById(R.id.button1);
        ActivityResultLauncher<Intent> activityResultLauncher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode()== Activity.RESULT_OK){
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                                ib1.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "prueba",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream= new ByteArrayOutputStream();
                if (bitmap!=null){
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    //......
                    //creating a post request
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="https://datamoviles.tk/volley/upload.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("rey", "onResponse: +"+ response);
                                    if (response.equals("success")){
                                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Failed to  Upload Image:"+response, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("rey", "That didn't work!");
                            Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("image", base64Image);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                    //....

                }else{
                    Toast.makeText(MainActivity.this, "Select image first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}