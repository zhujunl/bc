package com.bc.sdk.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bc.sdk.model.bean.ChatBean;
import com.example.demo_bckj.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ZJL
 * @date 2022/12/15 14:32
 * @des 客服聊天记录Adapter
 * @updateAuthor
 * @updateDes
 */
public class CSAdapter extends RecyclerView.Adapter<CSAdapter.MyViewHolder> {

    private Context context;
    private List<ChatBean> list;

    public CSAdapter(Context context, List<ChatBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_cs_cs,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!list.get(position).isMine()) {
            holder.STxt.setVisibility(View.VISIBLE);
            holder.STxt.setText(list.get(position).getTxt());
        } else {
            holder.CTxt.setVisibility(View.VISIBLE);
            holder.CTxt.setText(list.get(position).getTxt());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(ChatBean bean){
        list.add(bean);
        notifyItemInserted(list.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView STxt;
        private TextView CTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            STxt = itemView.findViewById(R.id.chatTxt);
            CTxt = itemView.findViewById(R.id.chatTxt2);
        }
    }
}
