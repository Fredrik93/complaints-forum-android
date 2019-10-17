package se.chalmers.cse.dit341.group00;

import android.content.Intent;
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
import se.chalmers.cse.dit341.group00.model.Room;


public class MainActivity extends AppCompatActivity {

    String text;

    Button submitButton;

    EditText roomName;

    // Field for parameter name
    public static final String HTTP_PARAM = "httpResponse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView mRoomView = findViewById(R.id.roomTextView);



        roomName = (EditText) findViewById(R.id.RoomNameInput);

        submitButton = (Button) findViewById(R.id.submitButton);
        RequestQueue queue = Volley.newRequestQueue(this);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = roomName.getText().toString();

                String url = getString(R.string.server_url) + "/api/rooms";
                JSONObject postParams = new JSONObject();
                try{
                    postParams.put("name", text);
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }

                JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, postParams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                              mRoomView.setText("Room " + text + " Created ! ");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                queue.add(jsonObjectReq);
            }
        });

    }

    public void ShowUser(View view){
        Intent intent = new Intent (this, UserActivity.class);
        startActivity(intent);
    }
    public void showPosts(View view){
        Intent intent = new Intent (this, PostActivity.class);
        startActivity(intent);
    }

    public void showRooms(View view){
        Intent intent = new Intent (this, RoomActivity.class);
        startActivity(intent);
    }

    public void onClickNewRoom (View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.server_url) + "/api/rooms";
        JSONObject postParams = new JSONObject();
        try{
            postParams.put("name", "london");
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(jsonObjectReq);
                //TextView mCamelView = findViewById(R.id.camelTextView);
                // Starts a new activity, providing the text from my HTTP text field as an input
                //Intent intent = new Intent(this, PostActivity.class);
                //intent.putExtra(HTTP_PARAM, mCamelView.getText().toString());
                //startActivity(intent);
    }

    public void onClickGetRooms (View view) {
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

    public void onClickDeleteRoom(View view) {
        final TextView myRoomView = findViewById(R.id.roomTextView);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.server_url) + "/api/rooms";
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        myRoomView.setText("Rooms deleted! ");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(deleteRequest);
    }
}
