package com.lost.lost.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lost.lost.R;
import com.lost.lost.javaRes.friend.Friend;
import com.lost.lost.javaRes.friend.ViewHolder;

import java.util.ArrayList;


public class FriendsFragment extends FragmentPassObject {


    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;

    private String uid = FirebaseAuth.getInstance().getUid();

    private Switch aSwitch;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = mDatabase.child("Users/").child(uid).child("Friends/");

    private FirebaseRecyclerAdapter adapter;

    private ArrayList<Friend> friendlist = new ArrayList<>();

    private boolean checked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = v.findViewById(R.id.my_recycler_view);
        myRef.keepSynced(true);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Friend> options = new FirebaseRecyclerOptions.Builder<Friend>()
                .setQuery(myRef, Friend.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Friend, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
               View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_friends, viewGroup, false);

               return new ViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Friend model) {
                friendlist.add(model);
                holder.setNameText(model.getName());
                aSwitch = holder.getaSwitch();

                for (Friend f : friendlist) {

                    aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                checked = true;
                            } else {
                                checked = false;
                            }
                        }
                    });
                    f.setEnabled(checked);
                }

            }
        };

        recyclerView.setAdapter(adapter);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void addFriend(Friend friend){
        friendlist.add(friend);
    }


    public ArrayList<Friend> getFriendsList() {
        return friendlist;
    }

}
