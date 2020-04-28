package com.Qaabel.org.view.adapter.Recycler;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.Qaabel.org.model.entities.LastChatModel;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.Qaabel.org.R;
import com.Qaabel.org.model.entities.FriendModel;
import com.Qaabel.org.view.fragment.MainActivity.flash.FlashesFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>
{

    List<FriendModel> users = new ArrayList<>();
    FlashesFragment mView;
    private FriendProfileViewModel profileViewModel;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView profileImg;
        TextView name_et;
        ImageView freind_chat;
        //        public CardView orderCardView;

        public ViewHolder(View view)
        {
            super(view);
            name_et = view.findViewById(R.id.name);
            profileImg = view.findViewById(R.id.profile_img);
            freind_chat = view.findViewById(R.id.chat_user_status);

        }
    }

    public FriendsAdapter(FlashesFragment mView)
    {
        this.mView = mView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_friends, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {

        holder.name_et.setText(users.get(position).getName());
        Picasso.get().load(Uri.parse(users.get(position).getImage())).into(holder.profileImg);
        RxView.clicks(holder.profileImg).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("FRIEND_USER",users.get(position));
                    bundle.putString("SOURCE","FRIENDS ADAPTER");
                   Navigation.findNavController(mView.getActivity(),R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_flash_to_navigation_user_profile, bundle);
                });
        RxView.clicks(holder.freind_chat).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("CHAT_ID",users.get(position).getChat());
                    bundle.putParcelable("FRIEND_USER",users.get(position));
                    Navigation.findNavController(mView.getActivity(),R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_flash_to_navigation_chat,bundle);

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

}

