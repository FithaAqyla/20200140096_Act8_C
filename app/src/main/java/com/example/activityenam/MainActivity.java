package com.example.activityenam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.activityenam.adapter.TemanAdapter;
import com.example.activityenam.database.DBController;
import com.example.activityenam.database.Teman;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TemanAdapter adapter;
    private ArrayList<Teman> temanArrayList = new ArrayList<>();
    DBController controller = new DBController(this);

    private static final String TAG = MainActivity.class.getSimpleName();
    public static String url_select = "http://10.0.2.2/umyti/bacateman.php";
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_TELPON = "telpon";

    private FloatingActionButton fab;
    String id,nm,tlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.floatingButton);
        BacaData();
        adapter = new TemanAdapter(temanArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TemanBaru.class);
                startActivity(intent);
            }
        });
    }

    private void BacaData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Teman item = new Teman();
                        item.setId(obj.getString(TAG_ID));
                        item.setNama(obj.getString(TAG_NAMA));
                        item.setTelpon(obj.getString(TAG_TELPON));

                        temanArrayList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "gagal",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jArr);


//        ArrayList<HashMap<String, String>> daftarTeman = controller.getAllTeman();
//        temanArrayList = new ArrayList<>();
//        for(int i=0; i<daftarTeman.size();i++){
//            Teman teman = new Teman();
//            teman.setId(daftarTeman.get(i).get("id").toString());
//            teman.setNama(daftarTeman.get(i).get("nama").toString());
//            teman.setTelpon(daftarTeman.get(i).get("telpon").toString());
//            temanArrayList.add(teman);
//        }
    }
}