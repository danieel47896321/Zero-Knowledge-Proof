package com.example.cryptography_hw1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

public class PopUpMSG {
    private AlertDialog.Builder Builder;
    public PopUpMSG(Context context, String Title, String Message){
        Builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        Builder.setTitle(Title);
        Builder.setMessage(Message);
        Builder.setCancelable(false);
        Builder.create().show();
    }
}