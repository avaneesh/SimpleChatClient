package com.avklabs.simplechat;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.avklabs.simplechat.Models.Message;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends ActionBarActivity {

    public static String sUserId;
    public static final String TAG = ChatActivity.class.getName();

    public static final String USER_ID_KEY = "userID";
    public static final String BODY_KEY = "body";

    public static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    private Handler handler = new Handler();

    ChatAdapter aMessages;
    ArrayList <Message> messages;
    ListView lvMessages;

    EditText etMessage;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (ParseUser.getCurrentUser() != null){
            startWithCurrentUser(); // Use current user
        }
        else {
            login(); // login with new user
        }

        // periodically get messages.
        //handler.postDelayed(runnable, 100);
    }

    private void startWithCurrentUser() {
        sUserId = ParseUser.getCurrentUser().getObjectId();

        setupMessagePosting();
    }

    private void setupMessagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        saveButton = (Button) findViewById(R.id.btnSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ParseObject parseObject = new ParseObject("Message");
                //parseObject.put(USER_ID_KEY, sUserId);
                //parseObject.put(BODY_KEY, etMessage.getText().toString());
                Message message = new Message();
                message.setBody(etMessage.getText().toString());
                message.setUserId(sUserId);
                etMessage.setEnabled(false);
                saveButton.setEnabled(false);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Toast.makeText(getApplicationContext(), "Send Failed :(", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Send Successful!!!", Toast.LENGTH_SHORT).show();
                            etMessage.setText("");
                            receiveMessages(); // get messages and load into UI
                        }
                        etMessage.setEnabled(true);
                        saveButton.setEnabled(true);
                    }
                });
            }
        });

        messages = new ArrayList<>();
        aMessages = new ChatAdapter(this, sUserId, messages);
        lvMessages = (ListView) findViewById(R.id.lvMessages);
        lvMessages.setAdapter(aMessages);
    }

    // get messages and load into adapater to populate ListView
    public void receiveMessages() {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Could not get messages");
                }
                else {
                    aMessages.clear();
                    aMessages.addAll(messages);
                    lvMessages.invalidate();
                }
            }
        });
    }


    // use annonymous user
    private void login () {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e != null){
                    e.printStackTrace();
                    Log.e(TAG, "Login failed!!!");
                }
                else {
                    startWithCurrentUser();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
