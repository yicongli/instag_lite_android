package com.unimelb.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import com.unimelb.adapter.SearchListAdapter;
import com.unimelb.constants.CommonConstants;
import com.unimelb.entity.BasicUserProfile;
import com.unimelb.instagramlite.R;
import com.unimelb.net.ErrorHandler;
import com.unimelb.net.HttpRequest;
import com.unimelb.net.IResponseHandler;
import com.unimelb.net.ResponseModel;
import com.unimelb.net.model.User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity {
    private SearchActivity context;

    private RecyclerView listView;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.search_back).setOnClickListener((view) -> finish());
        editText = findViewById(R.id.search_edit_text);
        listView = findViewById(R.id.search_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            private Timer timer = new Timer();
            private final long DELAY = 500;

            @Override
            public void afterTextChanged(Editable editable) {
                List<BasicUserProfile> searchResultList = new ArrayList<>();
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (editable.toString().length() > 0) {
                                    HttpRequest.getInstance().doGetRequestAsync(CommonConstants.IP + "/api/v1/search/users/" + editable.toString(), null, new IResponseHandler() {
                                        @Override
                                        public void onFailure(int statusCode, String errJson) {
                                            new ErrorHandler(context).handle(statusCode, errJson);
                                        }

                                        @Override
                                        public void onSuccess(String json) {
                                            System.out.println(json);
                                            ResponseModel rm = new ResponseModel(json);
                                            JSONObject data = rm.getData();
                                            JSONArray users = (JSONArray) data.get("users");
                                            List<User> userList = new ArrayList<>();
                                            for (int i = 0; i < users.size(); i++) {
                                                User user = new User(users.get(i).toString());
                                                userList.add(user);
                                            }

                                            context.runOnUiThread(() -> {
                                                for (User user : userList) {
                                                    BasicUserProfile profile = new BasicUserProfile(user.getId(), user.getAvatarUrl(), user.getUsername(), user.getBio());
                                                    searchResultList.add(profile);
                                                }
                                                listView.setAdapter(new SearchListAdapter(context, searchResultList));
                                            });
                                        }
                                    });
                                } else {
                                    context.runOnUiThread(()->{
                                        searchResultList.clear();
                                        listView.setAdapter(new SearchListAdapter(context, searchResultList));
                                    });
                                }
                            }
                        }, DELAY
                );
            }
        });
    }
}
