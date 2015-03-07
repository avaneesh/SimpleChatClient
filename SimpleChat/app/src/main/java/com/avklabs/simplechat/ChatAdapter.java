package com.avklabs.simplechat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avklabs.simplechat.Models.Message;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by avkadam on 3/4/15.
 */
public class ChatAdapter extends ArrayAdapter<Message> {

    private String myUserId;


    public ChatAdapter(Context context, String userID, List<Message> messages) {
        super(context, 0, messages);
        myUserId = userID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.ivLeft = (ImageView) convertView.findViewById(R.id.ivLeft);
            viewHolder.ivRight = (ImageView) convertView.findViewById(R.id.ivRight);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            convertView.setTag(viewHolder);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        Message message = getItem(position);

        Boolean isMe = message.getUserId().equals(myUserId);

        ImageView imageView;

        if (isMe) {
            viewHolder.ivRight.setVisibility(View.VISIBLE);
            viewHolder.ivLeft.setVisibility(View.GONE);
            viewHolder.tvBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            imageView = viewHolder.ivRight;
        }
        else {
            viewHolder.ivLeft.setVisibility(View.VISIBLE);
            viewHolder.ivRight.setVisibility(View.GONE);
            viewHolder.tvBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            imageView = viewHolder.ivLeft;
        }

        imageView.setImageResource(0);

        Picasso.with(getContext()).load(getProfileUrl(message.getUserId())).into(imageView);
        viewHolder.tvBody.setText(message.getBody());


        return convertView;
    }

    private String getProfileUrl(String userId) {
        String hex = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte [] hash = digest.digest(userId.getBytes());
            final BigInteger bigInteger = new BigInteger(hash);
            hex = bigInteger.abs().toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    public class ViewHolder {
        ImageView ivLeft;
        ImageView ivRight;
        TextView tvBody;
    }
}
