package com.example.demo_bckj.view.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.demo_bckj.R;
import com.example.demo_bckj.base.BaseFragment;
import com.example.demo_bckj.control.SDKListener;
import com.example.demo_bckj.model.bean.ChatBean;
import com.example.demo_bckj.presenter.CServicePresenter;
import com.example.demo_bckj.view.adapter.CSAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ZJL
 * @date 2022/12/14 14:44
 * @des 客服Fragment
 * @updateAuthor
 * @updateDes
 */
public class CServiceFragment extends BaseFragment<CServicePresenter> {

    public static CServiceFragment instance;

    private EditText edit;
    private ImageView img;
    private RecyclerView rv;
    private Button submit;
    private CSAdapter adapter;
    private Map<Integer, String> map = new HashMap<>();
    private List<ChatBean> datas = new ArrayList<>();
    private DrawerLayout drawerLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private static String TAG="CServiceFragment";

    public static CServiceFragment getInstance(SDKListener sdkListener, DrawerLayout drawerLayout) {
        if (instance == null) {
            instance = new CServiceFragment(drawerLayout);
        }
        return instance;
    }

    public CServiceFragment(DrawerLayout drawerLayout) {
        this.drawerLayout=drawerLayout;
    }


    @Override
    protected void initData() {
        Log.d(TAG, "initData" );
//        for (int i = 0; i < 2; i++) {
//            if (i % 2 == 0) {
//                datas.add(new ChatBean(false, "您好，无限游戏客服很高兴为你服务~"));
//            } else {
//                datas.add(new ChatBean(true, "未成年防沉迷规则"));
//            }
//        }
        adapter = new CSAdapter(getContext(), datas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);//设置布局管理器
        adapter.setHasStableIds(true);
        rv.setAdapter(adapter);
    }

    @Override
    protected void initView() {
        edit = v.findViewById(R.id.cs_edit);
        img = v.findViewById(R.id.csImg);
        rv = v.findViewById(R.id.cs_rv);
        submit = v.findViewById(R.id.cs_btn);

        submit.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(edit.getText().toString().trim())) {
                adapter.add(new ChatBean(true, edit.getText().toString().trim()));
                rv.scrollToPosition(adapter.getItemCount()-1);
                edit.setText("");
            }
        });
    }

    @Override
    protected int initLayoutID() {
        return R.layout.fragment_cs;
    }

    @Override
    protected CServicePresenter initPresenter() {
        return new CServicePresenter();
    }

    @Override
    public void onSuccess(Object o) {

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy" );
        instance=null;
    }

    protected void finish() {
        if (fm.getBackStackEntryCount()>1){
            fm.popBackStack("CService", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            drawerLayout.closeDrawers();
            return;
        }
        System.exit(0);
    }
}
