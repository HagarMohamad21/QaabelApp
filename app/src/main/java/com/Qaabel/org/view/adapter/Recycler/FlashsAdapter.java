package com.Qaabel.org.view.adapter.Recycler;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.Qaabel.org.helpers.Common;
import com.Qaabel.org.helpers.TimeUtils;
import com.Qaabel.org.viewModel.viewModel.friend.FlashUserViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.entities.FriendModel;
import com.Qaabel.org.view.fragment.MainActivity.flash.FlashesFragment;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class FlashsAdapter extends RecyclerView.Adapter<FlashsAdapter.ViewHolder>
{

    List<FriendModel> users = new ArrayList<>();
    FlashesFragment mView;
    FlashUserViewModel flashUserViewModel;
    String token;
    FlashesFragment fragment;
    private static final String TAG = "FlashsAdapter";
    public class ViewHolder extends RecyclerView.ViewHolder
    {
         LinearLayout dataLayout;
        CircleImageView profileImg;
        TextView name_et, age_et;
        Button flashBackBtn, declineBtn;
        LinearLayout doneLayout;

        public ViewHolder(View view)
        {
            super(view);
            dataLayout=view.findViewById(R.id.data_layout);
            name_et = view.findViewById(R.id.freind_name);
            age_et = view.findViewById(R.id.friend_age);
            profileImg = view.findViewById(R.id.friend_img);
            flashBackBtn = view.findViewById(R.id.friend_action_btn);
            declineBtn = view.findViewById(R.id.decline_btn);
            doneLayout = view.findViewById(R.id.done_layout);

        }
    }

    public FlashsAdapter(FlashesFragment fragment)
    {
        this.mView = fragment;
        this.fragment = fragment;
        this.token = new SharedPref(mView.getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        this.flashUserViewModel = ViewModelProviders.of(mView.getActivity()).get(FlashUserViewModel.class);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_flashes, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        holder.doneLayout.setVisibility(View.GONE);

        holder.name_et.setText(users.get(position).getName());
        if(!users.get(position).getAgePrivate())
         holder.age_et.setText(getAge(users.get(position)));
        Picasso.get().load(Uri.parse(users.get(position).getImage())).into(holder.profileImg);
        RxView.clicks(holder.profileImg).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("FRIEND_USER", users.get(position));
                    bundle.putString("SOURCE","FLASHES");
                    Navigation.findNavController(mView.getActivity(), R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_flash_to_navigation_user_profile, bundle);
                });
        RxView.clicks(holder.flashBackBtn).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    flashUserViewModel.flashUserBack(token,users.get(position).getId()).observe(fragment,activeResponse ->
                    {
                       if(activeResponse!=null){
                           if(activeResponse.getStatus()==200){
                               Animation animation = AnimationUtils.loadAnimation(mView.getContext(), R.anim.item_animation_from_right);
                               holder.doneLayout.startAnimation(animation);
                               holder.doneLayout.setVisibility(View.VISIBLE);

                               new Handler().postDelayed(() -> {
                                   users.remove(position);
                                   notifyDataSetChanged();
                               }, 3000);
                           }
                       }
                    });
                });
        RxView.clicks(holder.declineBtn).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                 flashUserViewModel.ignoreUser(token,users.get(position).getId()).observe(fragment,activeResponse ->
                 { if(activeResponse!=null){
                     if(activeResponse.getStatus()==200){

                         Animation animation = AnimationUtils.loadAnimation(mView.getContext(), R.anim.item_animation_from_right);
                         holder.doneLayout.startAnimation(animation);
                        holder.doneLayout.setVisibility(View.VISIBLE);

                         new Handler().postDelayed(() -> {
                             users.remove(position);
                             notifyDataSetChanged();
                         }, 3000);
                 }}
                 });
                
                });

    }


    public void setFriendsList(ArrayList<FriendModel> users)
    {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        if (users.size() == 0)
        {
            return 0;
        }

        return users.size();
    }
    private String getAge(FriendModel friendUser){
    String age= Common.Companion.getAge(
            Integer.parseInt(friendUser.getBirthdayDate().substring(0,4))
            , Integer.parseInt(friendUser.getBirthdayDate().substring(5,7))
            , Integer.parseInt(friendUser.getBirthdayDate().substring(8,10)));
    return  "/"+age+"YO";
}
}

