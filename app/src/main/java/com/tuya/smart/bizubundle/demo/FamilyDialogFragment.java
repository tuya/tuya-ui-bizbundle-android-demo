package com.tuya.smart.bizubundle.demo;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.utils.ProgressUtil;
import com.tuya.smart.utils.ToastUtil;

import java.util.List;

/**
 * demo use only
 */
public class FamilyDialogFragment extends DialogFragment {
    private RecyclerView mFamilyRecycler;
    private BaseAdapter mAdapter;
    private TextView mAddFamily;

    public static FamilyDialogFragment newInstance() {

        Bundle args = new Bundle();
        FamilyDialogFragment fragment = new FamilyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), (int) (dm.heightPixels * 0.5));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_family, container, false);
        mFamilyRecycler = v.findViewById(R.id.recycler_family);
        mAddFamily = v.findViewById(R.id.fragment_add_family);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getHomeList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFamilyRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BaseAdapter();
        mFamilyRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new onItemClickListener() {
            @Override
            public void onClick(int position) {
                ((MainActivity) getActivity()).setCurrentFamily(mAdapter.getData().get(position));
                dismiss();
            }
        });
        mAddFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyCreateDialogFragment dialogFragment = FamilyCreateDialogFragment.newInstance();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "FamilyCreateDialogFragment");
                dismiss();
            }
        });
    }

    private void getHomeList() {
        ProgressUtil.showLoading(getActivity(), "Loading...");
        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> list) {
                if (!list.isEmpty()) {
                    mAdapter.setData(list);
                } else {
                    ToastUtil.showToast(getActivity(), "home list is null,plz create home");
                }
                ProgressUtil.hideLoading();
            }

            @Override
            public void onError(String s, String s1) {
                ProgressUtil.hideLoading();
                Toast.makeText(getActivity(), s + "\n" + s1, Toast.LENGTH_LONG).show();
            }
        });
    }


    static class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private onItemClickListener onItemClickListener;
        private TextView mFamilyName;

        public BaseViewHolder(@NonNull View itemView, FamilyDialogFragment.onItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            mFamilyName = itemView.findViewById(R.id.item_family_name);
            mFamilyName.setOnClickListener(this);
        }

        public void update(String name) {
            mFamilyName.setText(name);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(getAdapterPosition());
        }
    }

    static class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

        private List<HomeBean> mFamilies;
        private onItemClickListener mOnItemClickListener;

        public List<HomeBean> getData() {
            return mFamilies;
        }

        public void setOnItemClickListener(onItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        @NonNull
        @Override
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_family, parent, false), mOnItemClickListener);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            holder.update(mFamilies.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return mFamilies != null ? mFamilies.size() : 0;
        }

        public void setData(List<HomeBean> families) {
            mFamilies = families;
            notifyDataSetChanged();
        }

    }

    interface onItemClickListener {
        void onClick(int position);
    }
}
