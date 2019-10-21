package se.chalmers.cse.dit341.group00;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        final TextView myRoomView = findViewById(R.id.roomTextView);


        roomName = (EditText) findViewById(R.id.RoomNameInput);

        submitButton = (Button) findViewById(R.id.submitButton);
        RequestQueue queue = Volley.newRequestQueue(this);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = roomName.getText().toString();

                String url = getString(R.string.server_url) + "/api/rooms";
                JSONObject postParams = new JSONObject();
                if (text.isEmpty()) {
                    toastMessage("Enter something");
                } else {
                    try {

                        postParams.put("name", text);
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }

                    JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, postParams,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    myRoomView.setText("Room " + text + " Created ! ");
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    myRoomView.setText("Couldn't create room ! ");

                                }
                            });
                    queue.add(jsonObjectReq);
                }
            }
        });

    }

    public void ShowUser(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

    public void showPosts(View view) {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    public void showRooms(View view) {
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
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
                        Log.d("MainActivity", "Rooms not deleted");

                    }
                }
        );
        queue.add(deleteRequest);
    }

    public void toastMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
