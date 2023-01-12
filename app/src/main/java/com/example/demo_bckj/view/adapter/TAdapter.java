package com.example.demo_bckj.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo_bckj.R;
import com.example.demo_bckj.model.utility.SPUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ZJL
 * @date 2022/12/23 15:51
 * @des 手机号码下拉Adapter
 * @updateAuthor
 * @updateDes
 */
public class TAdapter extends RecyclerView.Adapter<TAdapter.MyHolder> {

    private Context context;
    private List<String> lists;
    telListener listener;

    public TAdapter(Context context, List<String> lists,telListener listener ){
        this.context = context;
        this.lists = lists;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_tel, parent, false);
        MyHolder myHolder = new MyHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tel.setText(lists.get(position));
        holder.btn.setOnClickListener(v->{
            lists.remove(position);
            notifyDataSetChanged();
            SPUtils.getInstance(context,"bcSP").put("tel",lists);
        });
        holder.itemView.setOnClickListener(v->{
            listener.itemClick(lists.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tel;
        Button btn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tel = itemView.findViewById(R.id.tel);
            btn = itemView.findViewById(R.id.tel_remove_btn);
        }
    }

    public interface telListener{
        void itemClick(String tel);
    }
}
