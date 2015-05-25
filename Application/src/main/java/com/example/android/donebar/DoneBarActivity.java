/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.donebar;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * A sample activity demonstrating the "done bar" alternative action bar presentation. For a more
 * detailed description see {@link R.string.done_bar_description}.
 */
public class DoneBarActivity extends Activity {

    EditText number;
    EditText information;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // BEGIN_INCLUDE (inflate_set_custom_view)
        // Inflate a "Done/Cancel" custom action bar view.
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done_cancel, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Done"
                        int id = generateId();
                        EditText number = (EditText) findViewById(R.id.caller_phone_number);
                        EditText information = (EditText) findViewById(R.id.caller_information);

                        checkAvailability(id, number.getText().toString(), information.getText().toString());

                        finish();
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Cancel"
                        finish();
                    }
                });

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        // END_INCLUDE (inflate_set_custom_view)


        setContentView(R.layout.activity_done_bar);
    }

    public void checkAvailability(int id, String number, String information) {
        if (Utility.isNotNull(number) && Utility.isNotNull(information)) {
            JSONObject json = new JSONObject();
            try {
                json.put("id", id);
                json.put("number", number);
                json.put("information", information);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to create JSON Object.", Toast.LENGTH_LONG).show();
            }

            sendJSON(json);

        } else {
            Toast.makeText(getApplicationContext(), "Please fill out all the blanks", Toast.LENGTH_LONG).show();
        }
    }

    public void sendJSON(JSONObject obj) {
        StringEntity se = null;
        try {
            se = new StringEntity(obj.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {

        }


        AsyncHttpClient client = new AsyncHttpClient();
        client.post(null, "http://192.168.0.103:8080/AndroidRESTful2/webresources/com.erikchenmelbourne.entities.caller", se, "application/json", new AsyncHttpResponseHandler() {
            private String string;

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                if (response != null)
                    try {
                        string = new String(response, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                else {
                    Toast.makeText(getApplicationContext(), "Status Code: " + statusCode + " Data has been posted.", Toast.LENGTH_LONG).show();
                    finish();
                }

                /*try {
                    JSONObject obj = new JSONObject(string);
                    if (obj.getBoolean("status")) {
                        setDefaultValues();
                        Toast.makeText(getApplicationContext(), "Information has been sent!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error Occurred. [Server's JSON response is invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (i == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found.", Toast.LENGTH_LONG).show();
                } else if (i == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occurred. [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public int generateId() {
        UUID idOne = UUID.randomUUID();
        String str = "" + idOne;
        int uid = str.hashCode();
        String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }

    public void setDefaultValues() {
        number.setText("");
        information.setText("");
    }
}
