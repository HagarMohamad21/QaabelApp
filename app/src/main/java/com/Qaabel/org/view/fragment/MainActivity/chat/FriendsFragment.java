package com.Qaabel.org.view.fragment.MainActivity.chat;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Qaabel.org.R;
import com.Qaabel.org.model.Api.Response.ApiFreindsResponse;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.entities.FriendModel;
import com.Qaabel.org.view.adapter.Recycler.FlashsAdapter;
import com.Qaabel.org.view.adapter.Recycler.FriendsAdapter;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class FriendsFragment extends Fragment
{
    RecyclerView recyclerView;
    TextView freindsTextView, flashesTextView;

    MutableLiveData<ApiFreindsResponse> mfreinds, mflshes;
    private FriendProfileViewModel profileViewModel;

    String token;

    FlashsAdapter flashsAdapter;
    FriendsAdapter friendsAdapter;

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener)
    {
        this.onDismissListener = onDismissListener;
    }

    public FriendsFragment()
    {
        // Required empty public constructor
    }
  @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_flashes, container, false);

        init(view);
        action();
        return view;
    }

    private void init(View view)
    {
        recyclerView = view.findViewById(R.id.firends_recycler);
        flashesTextView = view.findViewById(R.id.flashes_tv);
        freindsTextView = view.findViewById(R.id.friend_tv);

        profileViewModel = ViewModelProviders.of(this.getActivity()).get(FriendProfileViewModel.class);
        token = new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);

//        friendsAdapter = new FriendsAdapter(FlashesFragment.this);
//        flashsAdapter = new FlashsAdapter( FlashesFragment.this);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(friendsAdapter);

        getFlashes(token);
        getFriends(token);


    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void action()
    {
        RxView.clicks(flashesTextView).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    FillFlashes();
                });
        RxView.clicks(freindsTextView).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    FillFriends();
                });

    }

    @SuppressLint("ResourceAsColor")
    private void FillFlashes()
    {
        recyclerView.setAdapter(flashsAdapter);
        flashesTextView.setBackgroundResource(R.drawable.ic_underline_blue_text);
        flashesTextView.setTextColor(Color.BLUE);
        freindsTextView.setBackgroundResource(R.drawable.ic_underline_text);
        freindsTextView.setTextColor(R.color.black);
        getFlashes(token);
    }

    @SuppressLint("ResourceAsColor")
    private void FillFriends()
    {
        recyclerView.setAdapter(friendsAdapter);
        freindsTextView.setBackgroundResource(R.drawable.ic_underline_blue_text);
        freindsTextView.setTextColor(Color.BLUE);
        flashesTextView.setBackgroundResource(R.drawable.ic_underline_text);
        flashesTextView.setTextColor(R.color.black);
        getFriends(token);
    }

    private void getFlashes(String token)
    {
        profileViewModel.getFlashes(token).observe(this, apiFreindsResponse -> {
            if (apiFreindsResponse != null)
            {
                if (apiFreindsResponse.getFlashes().size() > 0)
                    flashsAdapter.setFriendsList((ArrayList<FriendModel>) apiFreindsResponse.getFlashes());
            }
        });
    }

    private void getFriends(String token)
    {
        profileViewModel.getFriends(token).observe(this, apiFreindsResponse -> {
            if (apiFreindsResponse != null)
            {
                if (apiFreindsResponse.getFriends().size() > 0)
                    friendsAdapter.setFriendsList((ArrayList<FriendModel>) apiFreindsResponse.getFriends());
            }
        });
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

}
