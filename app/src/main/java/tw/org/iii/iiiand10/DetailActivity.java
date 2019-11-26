package tw.org.iii.iiiand10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    private String id, name,addr, hostwords, feauture, latlng, picurl, tel, heart;
    private ImageView img;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();//收到任何intent;  MainActivity有intent只是沒有接收
        HashMap<String,String> row =
                (HashMap<String, String>)(intent.getSerializableExtra("data"));
        Log.v("brad",row.get("Name"));

        id = row.get("ID");
        name = row.get("Name");
        addr = row.get("Address");
        hostwords = row.get("HostWords");
        tel = row.get("Tel");
        latlng = row.get("Coordinate");
        feauture = row.get("FoodFeature");
        picurl = row.get("PicURL");
        heart = row.get("Heart");

        img = findViewById(R.id.detail_img);
        content = findViewById(R.id.detailContent);

        fetchRemoteImage();
    }

    //抓遠端影像資料; 抓資料是StringRequest
    private void fetchRemoteImage(){
        ImageRequest request = new ImageRequest(picurl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {//回覆在這
                        img.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MainApp.queue.add(request);//創物件後,提出要求
    }
}
