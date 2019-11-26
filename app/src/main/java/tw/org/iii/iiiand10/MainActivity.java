package tw.org.iii.iiiand10;
//加入我的最愛功能
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);//浮動視窗 物件生成
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading....");

        data = new LinkedList<>();
        queue = Volley.newRequestQueue(this);
        listView = findViewById(R.id.listView);
        initListView();
        fetchRemoteDate();
    }

    //拉取遠端資料抓進
    private void fetchRemoteDate(){
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, "http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvTravelFood.aspx",
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
                        progressDialog.dismiss();
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
                dd.put("Heart", "xx");//OK 屬於我的最愛; 否則xx
                //尋訪完畢,add至data
                data.add(dd);
            }
            myAdapter.notifyDataSetChanged();//一開始 沒資料, listener解完, 要通知版面配置變動
        }catch (Exception e){
            Log.v("brad",e.toString());
        }
        progressDialog.dismiss();

    }

    //初始,沒有資料
    private  void initListView(){
        myAdapter = new MyAdapter();//調變器
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoDetail(position);
            }
        });
    }

    private void gotoDetail(int index){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("data", data.get(index));
        startActivity(intent);
    }



    //抽象類別 調變器; 出現紅底線, create
    private class  MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //第幾個版面如何,不同資料內容,有不同版面
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this); //物件,浮現式,右鍵浮現視窗,只要不適固定版面配置的,多半為inflater
            //使用當時只會有一個, 沒有new
            View view = inflater.inflate(R.layout.item, null); //浮現

            TextView name = view.findViewById(R.id.item_name);
            name.setText(data.get(position).get("Name"));

            TextView tel = view.findViewById(R.id.item_tel);
            tel.setText(data.get(position).get("Tel"));

            TextView address = view.findViewById(R.id.item_address);
            address.setText(data.get(position).get("Address"));

            ImageView heart = view.findViewById(R.id.item_heart);
            heart.setImageResource(data.get(position).get("Heart").equals("ok")? R.drawable.heart : R.drawable.heart_no);
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.get(position).put("Heart", data.get(position).get("Heart").equals("ok")? "xx":"ok");//變更其值
                    ((ImageView)v).setImageResource(data.get(position).get("Heart").equals("ok")? R.drawable.heart : R.drawable.heart_no); //轉型為image view
                    Log.v("brad","pos:"+position);
                    //1.資料要正確  2.呈現也要能match
                }
            });

            return view;
        }
    }
}
