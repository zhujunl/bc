package com.bc.sdk.view.pop;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.bc.sdk.view.adapter.TAdapter;
import com.example.demo_bckj.R;

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
public class PopupTel<T> extends PopupWindow implements TAdapter.telListener {
    private static PopupTel instance;
    private Context context;
    private List<T> list;
    private RecyclerView rv;
    private TAdapter adapter;
    private EditText edit, edit2;
    private int style = 0;

    public PopupTel(Context context, List<T> lists, EditText edit, EditText edit2 , View v, int width, int height, boolean focusable, Button spinnerImg) {
        super(v, width, height, focusable);
        this.context = context;
        this.list = lists;
        this.edit = edit;
        this.edit2=edit2;
        setFocusable(true);
        rv = v.findViewById(R.id.tel_rv);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TAdapter(context, lists, this);
        rv.setAdapter(adapter);

        setOnDismissListener(() -> {
            spinnerImg.setBackgroundResource(R.mipmap.infinite_game_caret_down);
        });
    }

    @Override
    public void itemClick(String tel, String password) {
        dismiss();
        edit.setText(tel);
        edit.setSelection(tel.length());
        edit2.setText(password);
    }
}
