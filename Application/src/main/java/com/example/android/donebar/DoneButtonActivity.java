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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.example.android.donebar.Caller;

/**
 * A sample activity demonstrating the "done button" alternative action bar presentation. For a more
 * detailed description see {@link R.string.done_button_description}.
 */
public class DoneButtonActivity extends Activity implements ItemFragment.OnFragmentInteractionListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // BEGIN_INCLUDE (inflate_set_custom_view)
        // Inflate a "Done" custom action bar view to serve as the "Up" affordance.
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Done"
                        finish();
                    }
                });

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView);
        // END_INCLUDE (inflate_set_custom_view)

        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://192.168.0.101:8080/AndroidRESTful2/webresources/com.erikchenmelbourne.entities.caller", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,
                                  org.apache.http.Header[] headers,
                                  org.json.JSONArray response) {
                if (response != null) {
                    String string = response.toString();
                    try {
                        JSONArray jsonarray = new JSONArray(string);
                        populateArrayList(jsonarray, Caller.jsonarraylist);
                        /*for (int i = 0; i < Caller.jsonarraylist.size(); i++) {
                            Toast.makeText(getApplicationContext(), Caller.jsonarraylist.get(i).toString(), Toast.LENGTH_LONG).show();
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Status Code: " + statusCode + " Data has been posted.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse) {
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found.", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occurred. [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });

        setContentView(R.layout.activity_done_button);
    }

    // BEGIN_INCLUDE (handle_cancel)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.cancel, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                // "Cancel"
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // END_INCLUDE (handle_cancel)

    public void onFragmentInteraction(String id) {

    }

    public void populateArrayList(JSONArray array, ArrayList<JSONObject> arraylist) {
        for (int i = 0; i < array.length(); i++) {
            try {
                arraylist.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
