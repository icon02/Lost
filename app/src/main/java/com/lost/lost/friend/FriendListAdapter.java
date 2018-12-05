package com.lost.lost.friend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.lost.lost.R;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends ArrayAdapter<Friend> {

    private Context context;
    private List<Friend> friendsList = new ArrayList<Friend>();

    public FriendListAdapter(Context context, List<Friend> friendsList) {
        super(context, 0, friendsList);
        this.context = context;
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listView = convertView;

        if (listView == null){
            listView = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false);
        }
        ImageView profilePic = listView.findViewById(R.id.imageView2);

        Friend current = friendsList.get(position);
        TextView name = listView.findViewById(R.id.textView_name);
        name.setText(current.getName());


        Switch enable = listView.findViewById(R.id.switch1);


        return listView;
    }
}
