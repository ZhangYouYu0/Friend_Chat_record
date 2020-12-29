package com.example.chat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;

public class MyFriendChatrecordAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<EMMessage> list;
    String name;
    public MyFriendChatrecordAdapter(Context context, ArrayList<EMMessage> list, String edname) {
        this.context = context;
        this.list = list;
        name=edname;
    }

    private static final int TYPE_ONE =1;
    private static final int TYPE_TWO =2;

    @Override
    public int getItemViewType(int position) {
        String from = list.get(position).getFrom();
        if(from.equals(name)){
            return TYPE_ONE;
        }else{
            return TYPE_TWO;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_ONE){
            View root = LayoutInflater.from(context).inflate(R.layout.item_add, parent, false);
            return new AddViewHolder1(root);
        }

        if (viewType==TYPE_TWO){
            View root = LayoutInflater.from(context).inflate(R.layout.item_add1, parent, false);
            return new AddViewHolder2(root);
        }
            return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case TYPE_ONE:
                AddViewHolder1 holder1= (AddViewHolder1) holder;
                EMTextMessageBody body = (EMTextMessageBody) list.get(position).getBody();
                holder1.tvAddNames1.setText(body.getMessage());
                break;
            case TYPE_TWO:
                AddViewHolder2 holder2 = (AddViewHolder2) holder;
                EMTextMessageBody body1 = (EMTextMessageBody) list.get(position).getBody();
                holder2.tvAddNames2.setText(body1.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class AddViewHolder1 extends RecyclerView.ViewHolder {
        TextView tvAddNames1;
        public AddViewHolder1(View root) {
            super(root);
            tvAddNames1 =root.findViewById(R.id.tv_add_names);
        }
    }

    private class AddViewHolder2 extends RecyclerView.ViewHolder {
       TextView tvAddNames2;
        public AddViewHolder2(View root) {
            super(root);
            tvAddNames2 = root.findViewById(R.id.tv_add_names1);
        }
    }
}
