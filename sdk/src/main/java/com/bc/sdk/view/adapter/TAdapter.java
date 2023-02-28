package com.bc.sdk.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bc.sdk.R;
import com.bc.sdk.db.entity.AccountLoginEntity;
import com.bc.sdk.db.entity.TelEntity;
import com.bc.sdk.manager.DBManager;

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
public class TAdapter<T> extends RecyclerView.Adapter<TAdapter<T>.MyHolder> {

    private Context context;
    private List<T> lists;
    telListener listener;

    public TAdapter(Context context, List<T> lists, telListener listener) {
        this.context = context;
        this.lists = lists;
        this.listener = listener;
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
        if (lists.get(position) instanceof AccountLoginEntity) {
            AccountLoginEntity item = (AccountLoginEntity) lists.get(position);
            holder.tel.setText(item.getAccount());
            holder.btn.setOnClickListener(v -> {
                lists.remove(position);
                notifyDataSetChanged();
                DBManager.getInstance(context).deleteAccountLogin(item);
            });
            holder.itemView.setOnClickListener(v -> {
                listener.itemClick(item.getAccount(), item.getPassword());
            });
        } else {
            TelEntity item = (TelEntity) lists.get(position);
            holder.tel.setText(item.getTelNumber());
            holder.btn.setOnClickListener(v -> {
                lists.remove(position);
                notifyDataSetChanged();
                DBManager.getInstance(context).deleteTel(item);
            });
            holder.itemView.setOnClickListener(v -> {
                listener.itemClick(item.getTelNumber(), "");
            });
        }
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

    public interface telListener {
        void itemClick(String tel, String password);
    }
}
