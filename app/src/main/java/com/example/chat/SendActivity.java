package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class SendActivity extends AppCompatActivity {

    private TextView req_frid_name;
    private Button acc_frid_req;
    private Button refuse_frid_req;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        String reason = intent.getStringExtra("reason");
        req_frid_name = findViewById(R.id.req_frid_name);
        acc_frid_req = findViewById(R.id.acc_frid_req);
        refuse_frid_req = findViewById(R.id.refuse_frid_req);

        req_frid_name.setText("姓名:"+ name +"理由："+reason);

        acc_frid_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(name);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });


        refuse_frid_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EMClient.getInstance().contactManager().declineInvitation(name);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}