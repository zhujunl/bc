package com.bc.sdk.view.dialog;

import android.app.AlertDialog;
import android.content.Context;

import com.bc.sdk.R;

/**
 * @author ZJL
 * @date 2023/3/6 16:03
 * @des
 * @updateAuthor
 * @updateDes
 */
public class MyDialog extends AlertDialog {
    public MyDialog(Context context) {
        super(context, R.style.myDialog);
        setCancelable(false);
    }
}
