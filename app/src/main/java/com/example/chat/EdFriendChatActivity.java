package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chat.Adapter.MyFriendChatrecordAdapter;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EdFriendChatActivity extends AppCompatActivity {

    private RecyclerView friend_chat_recycler;
    private EditText ed_chat_record;
    private Button bnt_chat_send;
    private ArrayList<EMMessage> emMessages;
    private MyFriendChatrecordAdapter addAdapter;
    private String edname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ed_friend_chat);
        Intent intent = getIntent();
        edname = intent.getStringExtra("name");
        initView();
    }

    private void initView() {
        friend_chat_recycler = findViewById(R.id.friend_chat_recycler);
        ed_chat_record = findViewById(R.id.ed_chat_record);
        bnt_chat_send = findViewById(R.id.bnt_chat_send);
        friend_chat_recycler.setLayoutManager(new LinearLayoutManager(this));

        emMessages = new ArrayList<>();

        addAdapter = new MyFriendChatrecordAdapter(this,emMessages,edname);

        friend_chat_recycler.setAdapter(addAdapter);

        ed_chat_record.setText("1313135");


        bnt_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ed_chat_record.getText().toString();
                EMMessage message = EMMessage.createTxtSendMessage(s,edname);
                EMClient.getInstance().chatManager().sendMessage(message);
                emMessages.addAll(Collections.singleton(message));
                addAdapter.notifyDataSetChanged();

            }
        });
        initjieshou();
    }

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };

    private void initjieshou() {
        EMClient.getInstance().chatManager().addMessageListener(msgListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }


}