package tw.org.iii.iiiand10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();//收到任何intent;  MainActivity有intent只是沒有接收
        HashMap<String,String> row =
                (HashMap<String, String>)(intent.getSerializableExtra("data"));
        Log.v("brad",row.get("Name"));

    }
}
