package se.chalmers.cse.dit341.group00;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import se.chalmers.cse.dit341.group00.model.Post;
import se.chalmers.cse.dit341.group00.model.Room;

public class RoomActivity extends AppCompatActivity {

    String text;

    Button submitButton;
    Button putButton;
    EditText roomName;

    Room[] rooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        // Get the text view in which we will show the result.
        final TextView myRoomView = findViewById(R.id.roomTextView);


        String url = getString(R.string.server_url) + "/api/rooms";

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
                            dataArray = response.getString("rooms");
                        } catch (JSONException e) {
                            Log.e(this.getClass().toString(), e.getMessage());
                        }

                        StringBuilder roomString = new StringBuilder();
                        roomString.append("This is the list of my rooms: \n");

                        rooms = gson.fromJson(dataArray, Room[].class);
                        for (Room currentRoom : rooms) {
                            roomString.append("Room " + currentRoom.name + "\n");
                            for (Post posti : currentRoom.posts) {
                                roomString.append("   Post title: " + posti.title + "\n" + "   Post text: " + posti.text + "\n" + "========================" + "\n");
                            }
                        }

                        myRoomView.setText(roomString.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        myRoomView.setText("There are no rooms");
                        Log.d("RoomActivity", "No Rooms in array");
                    }
                });
        // The request queue makes sure that HTTP requests are processed in the right order.
        queue.add(jsonObjectRequest);

        roomName = findViewById(R.id.RoomNameInput);
        putButton = findViewById(R.id.putButton);
        putButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = roomName.getText().toString();
                try {
                    for(Room currentRoom : rooms) {
                        if(currentRoom.name.equals(text)) {
                            String putURL = getString(R.string.server_url) + "/api/rooms/" + currentRoom._id;
                            JSONArray array = new JSONArray();
                            JSONObject roomJSON = new JSONObject();
                            try {
                                roomJSON.put("name", currentRoom.name);
                                roomJSON.put("users", array);
                                roomJSON.put("posts", array);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            JsonObjectRequest putRequest = new JsonObjectRequest (Request.Method.PUT, putURL, roomJSON,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("Response", response.toString());
                                            myRoomView.setText("Room reset");
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }
                            );
                            queue.add(putRequest);
                        }
                    }
                } catch (Exception e) {
                    toastMessage("You didn't specify a room name");
                    Log.d("RoomActivity",e.getMessage());
                }

            }
        });
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            text = roomName.getText().toString();
            try {
                for (Room currentRoom : rooms) {
                    if (currentRoom.name.equals(text)) {
                        String roomUrl = getString(R.string.server_url) + "/api/rooms/" + currentRoom._id;
                        System.out.println(roomUrl);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, roomUrl, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // GSON allows to parse a JSON string/JSONObject directly into a user-defined class
                                        Gson gson = new Gson();
                                        String dataArray = null;

                                        try {
                                            dataArray = response.getString("posts");
                                            System.out.println(dataArray);
                                        } catch (JSONException e) {
                                            Log.e(this.getClass().toString(), e.getMessage());
                                        }

                                        StringBuilder roomString = new StringBuilder();
                                        roomString.append("These are the posts in the room: " + currentRoom.name + "\n");
                                        Post [] posts = gson.fromJson(dataArray, Post[].class);
                                        //rooms = gson.fromJson(dataArray, Room[].class);
                                        for (Post currentPost : posts) {
                                            roomString.append("Title: " + currentPost.title + "\n" + "Text: " + currentPost.text + "\n" + "========================" + "\n");
                                        }

                                        myRoomView.setText(roomString.toString());
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        myRoomView.setText("There are no rooms");
                                    }
                                });
                        queue.add(jsonObjectRequest);
                    } else {
                        myRoomView.setText("This room does not exist");
                    }
                }
            } catch (Exception e) {
                toastMessage("You didn't specify a room name");
                Log.d("RoomActivity",e.getMessage());
            }

            }
        });
    }
    public void toastMessage(String message) {
        Toast.makeText(RoomActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}


