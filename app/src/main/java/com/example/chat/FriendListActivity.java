package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chat.Adapter.FriendListAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private RecyclerView friend_recycler;
    private FriendListAdapter friendListAdapter;
    private ArrayList<String> allContactsFromServer;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        initView();
        initData();
        initOnClickListener();
    }

    private void initOnClickListener() {
        friendListAdapter.setOnClickListener(new FriendListAdapter.OnClickListener() {
            @Override
            public void onClickListener(int pos) {
                String s = list.get(pos);
                Toast.makeText(FriendListActivity.this, s+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FriendListActivity.this, EdFriendChatActivity.class);
                intent.putExtra("name",list.get(pos));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                try {
                    allContactsFromServer = (ArrayList<String>) EMClient.getInstance().contactManager().getAllContactsFromServer();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list.addAll(allContactsFromServer);
                            friendListAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }



    private void initView() {

        friend_recycler=findViewById(R.id.friend_recycler);
        friend_recycler.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        friendListAdapter = new FriendListAdapter(list,this);
        friend_recycler.setAdapter(friendListAdapter);

    }

    public void find_friend_but(View view) {
        Intent intent = new Intent(FriendListActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }
}