package tw.org.iii.iiiand10;
//加入我的最愛功能,link sqlit
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private MyAdapter myAdapter;
    private RequestQueue queue;
    private LinkedList<HashMap<String,String>> data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new LinkedList<>();
        queue = Volley.newRequestQueue(this);
        listView = findViewById(R.id.listView);
        initListView();
        fetchRemoteDate();
    }

    //拉取遠端資料抓進
    private void fetchRemoteDate(){
        StringRequest request = new StringRequest(Request.Method.GET,
                "http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvTravelFood.aspx",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("brad",response);
                        parseJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("brad",error.toString());
                    }
                });
        queue.add(request);
    }

    //解析資料
    private void parseJSON(String json){
        try {
            JSONArray root = new JSONArray(json);
            for(int i=0; i<root.length(); i++){  //大括號是物件
                HashMap<String,String> dd = new HashMap<>();
                JSONObject  row = root.getJSONObject(i);
                dd.put("ID",row.getString("ID"));
                dd.put("Name",row.getString("Name"));
                dd.put("Address",row.getString("Address"));
                dd.put("HostWords",row.getString("HostWords"));
                dd.put("Tel",row.getString("Tel"));
                dd.put("Coordinate",row.getString("Coordinate"));
                dd.put("FoodFeature",row.getString("FoodFeature"));
                dd.put("PicURL",row.getString("PicURL"));
                //尋訪完畢,add至data
                data.add(dd);
            }
        }catch (Exception e){
            Log.v("brad",e.toString());
        }

    }

    private  void initListView(){
        myAdapter = new MyAdapter();//調變器
        listView.setAdapter(myAdapter);
    }


    //抽象類別 調變器; 出現紅底線, create
    private class  MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //第幾個版面如何
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
