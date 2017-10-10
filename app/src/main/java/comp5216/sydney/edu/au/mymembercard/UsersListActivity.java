package comp5216.sydney.edu.au.mymembercard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WZZ on 06/10/2017.
 */

public class UsersListActivity extends AppCompatActivity {
   /*
   In the first step, need to use emial, to initializea a user.
    */


    private AppCompatActivity activity = UsersListActivity.this;
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewUsers;
//    private List<User> listUsers;
    private List<MemberCard> memberCardList;
    private UsersRecyclerAdapter usersRecyclerAdapter;
//    private DatabaseHelper databaseHelper;
    private DBHelper dbHelper;
    public String emailFromIntent;
    private User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        getSupportActionBar().setTitle("");
        initViews();
        initObjects();

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        memberCardList = new ArrayList<>();

        dbHelper = new DBHelper();

        dbHelper.open();
        emailFromIntent = getIntent().getStringExtra("EMAIL");
        user=dbHelper.makeUser(emailFromIntent);
        dbHelper.close();

        getDataFromSQLite();

        textViewName.setText(user.getName());

        usersRecyclerAdapter = new UsersRecyclerAdapter(memberCardList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersRecyclerAdapter);
//        databaseHelper = new DatabaseHelper(activity);





    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                memberCardList.clear();
//                listUsers.addAll(databaseHelper.getAllUser());

                memberCardList=user.getMemberCardList();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                usersRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
