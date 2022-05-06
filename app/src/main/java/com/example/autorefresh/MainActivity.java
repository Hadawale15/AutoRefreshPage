package com.example.autorefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    String imagePath;
    private String Url="http://3.7.220.114:4000/reporteddata";
    //private String Url="https://picsum.photos/200/300";
    private ImageView refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.image_id);
        List<SliderItem> sliderItems=new ArrayList<>();

        refresh=findViewById(R.id.refresh_button_id);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Refresh2();
            }
        });

        content();


      //  Toast.makeText(MainActivity.this, sliderItems.get(0).toString(), Toast.LENGTH_SHORT).show();

    }

    private void content() {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest=new JsonArrayRequest(Request.Method.GET, Url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                    try {
                        JSONObject object=response.getJSONObject(0);
                        SliderItem model=new SliderItem(object.getString("imagePath"));
                        imagePath= model.getImage();

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

              Glide.with(getApplicationContext()).load(imagePath).into(imageView);
                Picasso.get().load(imagePath).into(imageView);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(MainActivity.this, "12", Toast.LENGTH_SHORT).show();
        requestQueue.add(arrayRequest);
        Refresh(3000);

    }

    private void Refresh(int milliseconds) {
        final Handler handler=new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable,milliseconds);
    }
    private void Refresh2() {
                content();
    }

}