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

public class RoomActivity extends AppCompatActivity {

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

                        Room[] rooms = gson.fromJson(dataArray, Room[].class);
                        for (Room currentRoom : rooms) {
                            roomString.append("Room " + currentRoom.name + "\n");
                            for (Post posti : currentRoom.posts) {
                                roomString.append("   "+ posti.text + " " + posti.userId + "\n");
                            }
                        }

                        myRoomView.setText(roomString.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        myRoomView.setText("There are no rooms");
                    }
                });

        // The request queue makes sure that HTTP requests are processed in the right order.
        queue.add(jsonObjectRequest);
    }
}