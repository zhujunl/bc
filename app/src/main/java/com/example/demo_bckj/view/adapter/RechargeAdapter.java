package com.example.demo_bckj.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo_bckj.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ZJL
 * @date 2022/12/16 11:18
 * @des 充值选项Adapter
 * @updateAuthor
 * @updateDes
 */
public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.MyViewHolder> {
    private Context context;
    private int p=0;
    private List<String> lists;

    public RechargeAdapter(Context context, List<String> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_charge,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt.setText(lists.get(position));
        holder.itemView.setOnClickListener(v->{
            p=position+1;
            notifyDataSetChanged();
        });
        if (p==position+1){
            holder.select.setImageResource(R.mipmap.selected);
        }else {
            holder.select.setImageResource(R.mipmap.unselect);
        }
        switch (position) {
            case 0:
                holder.log.setImageResource(R.mipmap.alipay);
                break;
            case 1:
                holder.log.setImageResource(R.mipmap.wechatpay);
                break;

            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView log,select;
        private TextView txt;
        public MyViewHolder(@NonNull View v) {
            super(v);
            log=v.findViewById(R.id.recharge_img);
            select=v.findViewById(R.id.recharge_sel);
            txt=v.findViewById(R.id.recharge_txt);
        }
    }
}
