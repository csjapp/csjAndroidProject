package com.csjcom.csjandroidproject;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csjcom.csjandroidlibrary.base.CSJActivity;
import com.csjcom.csjandroidlibrary.http.CSJHttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends CSJActivity implements CSJHttpHelper.CSJHttpPostCallBack {

    MainListAdapter adapter;
    ListView mainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainList = findViewById(R.id.main_list);
        adapter = new MainListAdapter();
        mainList.setAdapter(adapter);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AddExceptionsDialog(MainActivity.this, adapter.datas.get(position)).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    private void update() {
        try {
            CSJHttpHelper.post(this, "getStores", new JSONObject(), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHttpPostFailure(String errMsg) {
        Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHttpPostSucceed(String jRes) {
//        Toast.makeText(this,jRes,Toast.LENGTH_LONG).show();
        try {
            JSONObject jResponse = new JSONObject(jRes);
            JSONArray jDataArray = jResponse.getJSONArray("listData");
            adapter.datas.clear();
            for (int i = 0; i < jDataArray.length(); i++) {
                GJ gj = new GJ();
                JSONObject jGJ = jDataArray.getJSONObject(i);
                gj.setADDRESS(jGJ.getString("ADDRESS"));
                gj.setCITY(jGJ.getString("CITY"));
                gj.setID(jGJ.getString("ID"));
                gj.setMEMO(jGJ.getString("MEMO"));
                gj.setREQUESTID(jGJ.getString("REQUESTID"));
                gj.setSTORENAME(jGJ.getString("STORENAME"));
                gj.setSTORENO(jGJ.getString("STORENO"));
                gj.setTEL(jGJ.getString("TEL"));
                adapter.datas.add(gj);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MainListAdapter extends BaseAdapter {
        ArrayList<GJ> datas = new ArrayList<>();

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_main_list, null);
            TextView tvStore = convertView.findViewById(R.id.item_main_list_store);
            TextView tvAddress = convertView.findViewById(R.id.item_main_list_address);
            TextView tvMemo = convertView.findViewById(R.id.item_main_list_memo);
            GJ data = datas.get(position);
            tvStore.setText(data.getSTORENAME() + "(" + data.getSTORENO() + ")");
            tvAddress.setText(data.getADDRESS());
            tvMemo.setText(data.getMEMO() + "(" + data.getTEL() + ")");
            return convertView;
        }
    }
}
