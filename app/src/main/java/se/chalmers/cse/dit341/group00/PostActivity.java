package se.chalmers.cse.dit341.group00;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import se.chalmers.cse.dit341.group00.model.Post;

public class PostActivity extends AppCompatActivity {

    String text;

    Button submitButton;

    EditText postName;
    Post[] posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        final TextView myPostView = findViewById(R.id.postTextView);



        String url = getString(R.string.server_url) + "/api/posts";

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
                            dataArray = response.getString("posts");
                        } catch (JSONException e) {
                            Log.e(this.getClass().toString(), e.getMessage());
                        }

                        StringBuilder postString = new StringBuilder();
                        postString.append("Browse Posts: \n");

                        posts = gson.fromJson(dataArray, Post[].class);
                        //posts[0].
                        for (Post currentPost : posts) {
                            postString.append("Title " + currentPost.title + "\n" + currentPost.text + "\n" + " id is " + currentPost._id + "\n");

                        }

                        myPostView.setText(postString.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        myPostView.setText("There are no posts");
                    }
                });

        postName = (EditText) findViewById(R.id.nameInput);

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = postName.getText().toString();
                for (Post currentPost : posts) {
                    if(currentPost.title.equals(text)) {
                        String deleteUrl = getString(R.string.server_url) + "/api/posts" + "/" + currentPost._id;
                        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, deleteUrl,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("Response", response);
                                        myPostView.setText("Room deleted! ");

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        );
                        queue.add(deleteRequest);
                       /* String deleteInRoomUrl = getString(R.string.server_url) + "/api/rooms/" + currentPost.roomId + "posts/" + currentPost._id;
                        StringRequest deleteRequestInRoom = new StringRequest(Request.Method.DELETE, deleteInRoomUrl,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("Response", response);
                                        myPostView.setText("Room deleted! ");

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        );
                        queue.add(deleteRequestInRoom);
                        */
                    }
                }
            }
        });
        // The request queue makes sure that HTTP requests are processed in the right order.
        queue.add(jsonObjectRequest);
    }

    }
