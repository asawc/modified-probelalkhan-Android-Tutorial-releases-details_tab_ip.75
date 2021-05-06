package myapp.releaseActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;

import net.simplifiedcoding.simplifiedcoding.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtilsHC4;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import myapp.JSONParser;
import myapp.api.ApiBuilder;
import myapp.api.CallbackImpl;
import myapp.api.RetrofitApi;
import myapp.api.ResponseContainer;
import myapp.model.Employee;
import myapp.model.Release;
import myapp.model.ReleaseStatus;
import myapp.modelView.ReleaseView;
import retrofit2.Call;
import retrofit2.Response;

import static myapp.api.URLs.URL_GET_ALL_RELEASES;

public class ReleaseActivity extends AppCompatActivity {

    protected RecyclerView mRecyclerView;
    protected ReleaseAdapter mReleaseAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<ReleaseView> mReleaseViews;

    protected RetrofitApi mApiService;

    protected List<Release> mReleasesList;
    private final String GET_RELEASES_TAG = "GET_RELEASES";

    JSONParser jsonParser = new JSONParser();

    public ArrayList<HashMap<String, String>> releases;

    // JSON Node names
    private static final String TAG_SUCCESS = "error";
    private static final String TAG_MESSAGE = "message";

    private static final String TAG_ID = "id";

    private static final String TAG_EMPLOYEES = "employeeslist";

    private static final String TAG_EMPLOYEE = "employee";

    //private static final String TAG_NAME = "name";

    // employees JSONArray
    JSONArray employees = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_activity);

        mRecyclerView = findViewById(R.id.releasesRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);

        mApiService = new ApiBuilder().getApiService();
        mReleasesList = new ArrayList<Release>();
        mReleaseViews = new ArrayList<ReleaseView>();

        // get releases via async retrofit request
        // getReleases();

        mReleaseAdapter = new ReleaseAdapter(mReleaseViews);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mReleaseAdapter);

        releases = new ArrayList<HashMap<String, String>>();
        new getReleasesList().execute();

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        // getListView
        // ListView lv = getListView();
/*
        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {

                //  String id = ((TextView) view.findViewById(R.id.uid)).getText()
                //          .toString();

                // Intent in = new Intent(getBaseContext(), StatusList.class);
                // in.putExtra(TAG_ID, uid);

                // startActivity(in);
            }
        });
*/
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // tutaj nowa aktywność dodawania wydań
                Intent intent = new Intent(ReleaseActivity.this, AddReleaseActivityCustViewDial.class);
                startActivity(intent);
            }
        });
    }

    public void openReleaseInfoActivity(int releaseId) {
        Intent intent = new Intent(this, ReleaseInfoActivity.class);
        //bundle.putSerializable();
        intent.putExtra(TAG_ID, releaseId);
        startActivity(intent);
    }

    public void getReleases() throws IOException {
        Call<ResponseContainer<List<Release>>> call =  mApiService.getReleases();

        call.enqueue(new CallbackImpl<ResponseContainer<List<Release>>>(GET_RELEASES_TAG){
            @Override
            public void onResponse(Call<ResponseContainer<List<Release>>> call,
                                   Response<ResponseContainer<List<Release>>> response) {
                super.onResponse(call, response);

                mReleasesList = response.body().getObject();
                Log.d(getTag()+"NrItms", String.valueOf(mReleasesList.size()));
                Log.d(getTag()+"time", mReleasesList.get(1).getCreationDateTime().toString());
                for(Release rel : mReleasesList)
                    mReleaseViews.add(new ReleaseView(rel.getId(),
                            rel.getCreationDateTime(),
                            rel.getStatus().name(),
                            rel.getEmployee().getName(),
                            rel.getEmployee().getSurname()));
            }
        });
    }

    class getReleasesList extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ReleaseActivity.this.setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            // Building Parameters
            List<NameValuePair> parametres = new ArrayList<NameValuePair>();

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(URL_GET_ALL_RELEASES);
            String json = null;
            try {
                HttpResponse response = client.execute(request);
                HttpEntity httpEntity = response.getEntity();
                json = EntityUtilsHC4.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return json;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String json) {

            Boolean result = null;
            String message = null;

            // dismiss the dialog after getting all releases
            try {
                Log.d("All Releases: ", json);
                JSONObject jsonObj = new JSONObject(json);
                // wybranie tablicy releases
                result = jsonObj.getBoolean("error");
                message = jsonObj.getString("message");

                JSONArray jsonArray = jsonObj.getJSONArray("object");
                for (int i = 0; i < jsonArray.length(); i++) {
                    // Wybranie pojedyńczego obiektu w tablicy - release
                    JSONObject jsonObj2 = jsonArray.getJSONObject(i);
                    // Wybranie poszczególnych
                    // id= jsonObj.getString("id").toInteger ?? // id konwertuj na int
                    String id = jsonObj2.getString("id");
                    String date_creation = jsonObj2.getString("creationDate");
                    String status = jsonObj2.getString("status");
                    JSONObject empl = jsonObj2.getJSONObject("employee");
                    Employee employee = new Employee(empl.getInt("id"),
                            empl.getString("symbol"),empl.getString("name"),
                            empl.getString("surname")) ;

                    HashMap<String, String> map = new HashMap<String, String>();

                    Release rel = new Release(Integer.parseInt(id), employee,  ReleaseStatus.enumOfValue(status), //ReleaseStatus.valueOf(status),
                            null, date_creation, null);
                    mReleasesList.add(rel);
                    mReleaseViews.add(new ReleaseView(rel.getId(),
                            rel.getCreationDateTime(),
                            rel.getStatus().name(),
                            rel.getEmployee().getSurname(),
                            rel.getEmployee().getName()));

                    // adding each child node to HashMap key => value
                    map.put("id", id);
                    map.put("date_creation", date_creation);
                    map.put("status", status);
                    map.put("id_employee", String.valueOf(employee.getId()));


                    // adding HashList to ArrayList
                    //releases.add(map);
                }
                mReleaseAdapter.notifyDataSetChanged();
            } catch(Exception e) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getBaseContext(), "Error while parsing response - " + e.getMessage(),  Toast.LENGTH_LONG).show();
            }

            if (result != null && (result == false)) {

                ReleaseActivity.this
                        .setProgressBarIndeterminateVisibility(false);
                /*
                // updating UI from Background Thread
                runOnUiThread(new Runnable() {
                    public void run() {

                        ListAdapter adapter = new SimpleAdapter(
                                ReleaseActivity.this, releases,
                                R.layout.content_release, new String[] { //TAG_ID,
                                "id", "date_creation", "status", "surname", "name"}, new int[] { //R.id.id,
                                R.id.id_release, R.id.date_creation,  R.id.status, R.id.employee_surname, R.id.employee_name});
                        // updating listview
                        setListAdapter(adapter);
                    }
                });
*/

                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
                        .show();

            }
        }

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_list, menu);
        return true;
    }*/


    public class ReleaseAdapter extends RecyclerView.Adapter<ReleaseAdapter.ViewHolder> {

        private static final String TAG = "ReleaseAdapter";

        private List<ReleaseView> mReleaseView;

        // VievHolder do przechowywania widoków elementów listy
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView mIdTextView;
            private final TextView mDateTimeTextView;
            private final TextView mReleaseStatusTextView;
            private final TextView mEmployeeSurnameTextView;
            private final TextView mEmployeeNameTextView;

            public ViewHolder(View v) {
                super(v);

                mIdTextView = (TextView) v.findViewById(R.id.id_release);
                mDateTimeTextView = (TextView) v.findViewById(R.id.date_creation_2);
                mReleaseStatusTextView = (TextView) v.findViewById(R.id.release_status);
                mEmployeeSurnameTextView = (TextView) v.findViewById(R.id.employee_surname);
                mEmployeeNameTextView = (TextView) v.findViewById(R.id.employee_name);

                // Define click listener for the ViewHolder's View.
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                        // Open new Activity with extra data - releaseId
                        openReleaseInfoActivity(Integer.parseInt(
                                mIdTextView.getText().toString()));
                    }
                });
            }



            public TextView getIdTextView() {
                return mIdTextView;
            }

            public TextView getDateTimeTextView() {
                return mDateTimeTextView;
            }

            public TextView getReleaseStatusTextView() {
                return mReleaseStatusTextView;
            }

            public TextView getEmployeeSurnameTextView() {
                return mEmployeeSurnameTextView;
            }

            public TextView getEmployeeNameTextView() {
                return mEmployeeNameTextView;
            }
        }



        /**
         * Initialize the dataset of the Adapter.
         *
         * @param relView List<ReleaseView> containing the data to populate views to be used by RecyclerView.
         */
        public ReleaseAdapter(List<ReleaseView> relView) {
            mReleaseView = relView;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public ReleaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_release, parent, false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Log.d(TAG, "Element " + position + " set.");
            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            holder.getIdTextView().setText(String.valueOf(mReleaseView.get(position).getId()));
            holder.getDateTimeTextView().setText(mReleaseView.get(position).getCreationDateTime());
            holder.getReleaseStatusTextView().setText(String.valueOf(mReleaseView.get(position).getStatus()));
            holder.getEmployeeSurnameTextView().setText(mReleaseView.get(position).getSurname());
            holder.getEmployeeNameTextView().setText(mReleaseView.get(position).getName());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mReleaseView == null ? 0 : mReleaseView.size();
        }

        public List<ReleaseView> getReleaseViews() {
            return mReleaseView;
        }
    }
}