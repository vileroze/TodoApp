package com.example.todoapp.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ShowToast {
    public static void displayToast(Context mContext, String message){
        Toast toast =  Toast.makeText(mContext,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
