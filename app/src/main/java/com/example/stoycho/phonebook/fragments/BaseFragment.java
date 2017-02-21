package com.example.stoycho.phonebook.fragments;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.database.HistoryDatabaseComunication;
import com.example.stoycho.phonebook.database.UsersDatabaseCommunication;
import com.example.stoycho.phonebook.models.HistoryModel;
import com.example.stoycho.phonebook.models.UserModel;

import java.util.Calendar;

/**
 * Created by stoycho.petrov on 25/01/2017.
 */

public class BaseFragment extends Fragment {

    private final        int    MY_PERMISSIONS_REQUEST_CALL_PHONE       = 2;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    public void setFocusOfEditText(EditText view,final TextView textView) {
        if (view.getText().toString().equals("")) {
            ValueAnimator translationX = ObjectAnimator.ofFloat(textView, "translationY", textView.getTranslationY(), textView.getTranslationY() - 50);
            ValueAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 1f, 0.8f);
            ValueAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 1f, 0.8f);
            textView.setPivotX(0);
            textView.setPivotY(0);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(translationX, scaleX, scaleY);
            animatorSet.start();

            textView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.darker_gray));
        }
    }

    public void removeFocus(EditText view,TextView textView)
    {
        if(view.getText().toString().equals("")) {
            ValueAnimator translationY = ObjectAnimator.ofFloat(textView, "translationY", textView.getTranslationY(), textView.getTranslationY() + 50);
            ValueAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 0.8f, 1f);
            ValueAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 0.8f, 1f);
            textView.setPivotX(0);
            textView.setPivotY(0);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(translationY, scaleX, scaleY);
            animatorSet.start();

            textView.setTextColor(ContextCompat.getColor(getActivity(),android.R.color.black));
        }
    }

    public void callToNumber(String callingCode,UserModel userModel)
    {
        String number                                           = "+" + callingCode + userModel.getPhoneNumber();
        if(!number.equals("")) {

            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(getString(R.string.tel) + number));

            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);

                UsersDatabaseCommunication usersDatabaseCommunication   = UsersDatabaseCommunication.getInstance(getActivity());
                usersDatabaseCommunication.updateCallsCounts(userModel.getId(),userModel.getmCallsCount() + 1);

                Calendar calendar = Calendar.getInstance();
                HistoryModel historyModel = new HistoryModel(calendar.getTime().toString(),userModel.getId());

                HistoryDatabaseComunication historyDatabaseComunication = HistoryDatabaseComunication.getInstance(getActivity());
                historyDatabaseComunication.insertIntoHistoryTable(historyModel);
            } else
                checkPermissions();
        }
    }

    private void checkPermissions()
    {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), R.string.permission_denied_phone_call, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
