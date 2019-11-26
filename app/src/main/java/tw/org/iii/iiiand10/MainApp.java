package tw.org.iii.iiiand10;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainApp extends Application { //mainapp也有生命週期
    public static RequestQueue queue;

    @Override
    public void onCreate() { //類別方法
        super.onCreate();
        queue = Volley.newRequestQueue(this);
    }
}
