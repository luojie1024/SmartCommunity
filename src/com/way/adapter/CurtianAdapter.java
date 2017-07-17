package com.way.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.way.tabui.gokit.R;
import com.way.util.CurtainInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/14.
 */

public class CurtianAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CurtainInfo> mList;

    private DatabaseAdapter dbAdapter;
    protected static final int UPDATA = 99;
    protected static final int DELETE = 100;
    Handler handler = new Handler();

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public CurtianAdapter (Handler handler, Context mContext) {
        this.handler = handler;
        this.dbAdapter =new DatabaseAdapter(mContext);
        this.mContext = mContext;
        this.mList = new ArrayList<CurtainInfo>();
    }

    public  CurtianAdapter (Context mContext, ArrayList<CurtainInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public CurtianAdapter setDate(String mac){
        mList=dbAdapter.findbybindgizCurtainInfo(mac);
        if(mList.size()!=0){
            this.notifyDataSetChanged();
            Log.i("smartoc", "setDate:-----> "+mList.size());
        }
        else
            return null;
        return this;
    }
    public CurtianAdapter updateList(CurtainInfo curtainInfo){
        if(curtainInfo!=null){
            dbAdapter.updateCurtainInfo(curtainInfo);
            return this;
        }
        return null;
    }

    public ArrayList<CurtainInfo> getmList(){
        return mList;
    }

    public CurtianAdapter deleteDate(int id){
        dbAdapter.deleteCurtainInfo(id);
        return this;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
       ViewHolder viewHolder =null;
        if(convertView==null){
            convertView = RelativeLayout.inflate(mContext, R.layout.listview_aircon_mes, null);
            viewHolder = new ViewHolder();
            viewHolder.tvAirName=(TextView) convertView.findViewById(R.id.tvAirName);
            viewHolder.bt_update=(Button) convertView.findViewById(R.id.bt_update);
            viewHolder.bt_delete=(Button) convertView.findViewById(R.id.bt_delete);
            viewHolder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final int index;
        index=position;
        viewHolder.imageView1.setImageResource(R.drawable.ic_curtain);
        viewHolder.tvAirName.setText(mList.get(position).getName());
        viewHolder.bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what =UPDATA;
                message.arg1 = mList.get(index).getId();
                message.obj=mList.get(index);
                handler.sendMessage(message);
            }
        });
        viewHolder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what =DELETE;
                message.arg1 = mList.get(index).getId();
                handler.sendMessage(message);
            }
        });
        return convertView;
    }

    class ViewHolder{
        private TextView tvAirName;
        private ImageView imageView1;
        private Button bt_update;
        private Button bt_delete;

    }

}
