package se.chalmers.cse.dit341.group00;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import se.chalmers.cse.dit341.group00.model.Post;
import se.chalmers.cse.dit341.group00.model.Room;
import se.chalmers.cse.dit341.group00.model.User;

public class UserActivity extends AppCompatActivity {

    User [] users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        // Get the text view in which we will show the result.
        final TextView myUserView = findViewById(R.id.userView);


        String url = getString(R.string.server_url) + "/api/users";

        // This uses Volley (Threading and a request queue is automatically handled in the background)
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // GSON allows to parse a JSON string/JSONObject directly into a user-defined class
                        Gson gson = new Gson();
                        String dataArray = null;

                        try {
                            dataArray = response.getString("users");
                        } catch (JSONException e) {
                            Log.e(this.getClass().toString(), e.getMessage());
                        }

                        System.out.println(dataArray);
                        StringBuilder userString = new StringBuilder();
                        userString.append("Browse Users: \n");
                        users = gson.fromJson(dataArray, User[].class);
                        for (User currentUser : users) {
                            userString.append("Username: " + currentUser.username + "\n");
                        }
                        myUserView.setText(userString.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        myUserView.setText("There are no users");
                    }
                });
        // The request queue makes sure that HTTP requests are processed in the right order.
        queue.add(jsonObjectRequest);
    }
}
