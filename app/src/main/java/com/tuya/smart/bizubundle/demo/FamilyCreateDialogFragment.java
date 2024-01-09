package com.tuya.smart.bizubundle.demo;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.bean.HomeBean;
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback;
import com.thingclips.smart.utils.ToastUtil;

import java.util.Arrays;

/**
 * demo use only
 */
public class FamilyCreateDialogFragment extends DialogFragment {

    private EditText mFamilyName;
    private Button mConfirm;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), (int) (dm.heightPixels * 0.25));
        }
    }

    public static FamilyCreateDialogFragment newInstance() {

        Bundle args = new Bundle();

        FamilyCreateDialogFragment fragment = new FamilyCreateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_family_create, container, false);
        mFamilyName = v.findViewById(R.id.fragment_input_family_name);
        mConfirm = v.findViewById(R.id.fragment_family_confirm);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mFamilyName.getText().toString().trim();
                ThingHomeSdk.getHomeManagerInstance().createHome(name, 0, 0, name, Arrays.asList("home"), new IThingHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {
                        // do something
                        dismiss();
                        ToastUtil.showToast(getActivity(), "Add family success");
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        // do something
                        ToastUtil.showToast(getActivity(), "errorCode: " + errorCode + "\nerrorMsg: " + errorMsg);
                    }
                });
            }
        });

    }
}
