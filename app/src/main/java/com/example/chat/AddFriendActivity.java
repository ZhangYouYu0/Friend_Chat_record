package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class AddFriendActivity extends AppCompatActivity {
    private EditText ed_user_nameID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ed_user_nameID = findViewById(R.id.ed_user_nameID);

    }

    public void bnt_Addriend(View view) {
         String nameId = ed_user_nameID.getText().toString();
        if(!TextUtils.isEmpty(nameId)){

            try {
                EMClient.getInstance().contactManager().addContact(nameId, "reason");
                EMClient.getInstance().contactManager().acceptInvitation(nameId);
                Log.e("tag","添加成功");
                Toast.makeText(AddFriendActivity.this, "发送申请", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddFriendActivity.this, FriendListActivity.class);
                startActivity(intent);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(this, "不能输入空ID", Toast.LENGTH_SHORT).show();
        }

    }



}