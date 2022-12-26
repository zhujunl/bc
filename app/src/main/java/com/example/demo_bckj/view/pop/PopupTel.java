package com.example.demo_bckj.view.pop;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.demo_bckj.R;
import com.example.demo_bckj.view.adapter.TAdapter;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ZJL
 * @date 2022/12/23 15:46
 * @des 手机号码下拉（Popup+TextView）
 * @updateAuthor
 * @updateDes
 */
public class PopupTel extends PopupWindow implements TAdapter.telListener{
    private Context context;
    private List<String> list;
    private RecyclerView rv;
    private TAdapter adapter;
    private EditText edit;

    public PopupTel(Context context, List<String> lists,EditText edit,View v, int width, int height, boolean focusable) {
        super(v, width, height, focusable);
        this.context = context;
        this.list=lists;
        this.edit=edit;
        setFocusable(true);
        rv=v.findViewById(R.id.tel_rv);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter=new TAdapter(context,lists,this);
        rv.setAdapter(adapter);
    }

    @Override
    public void itemClick(String tel) {
        dismiss();
        edit.setText(tel);
        edit.setSelection(tel.length());
    }
}
