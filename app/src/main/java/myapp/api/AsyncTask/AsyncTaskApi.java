package myapp.api.AsyncTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.simplifiedcoding.simplifiedcoding.RequestHandler;
import net.simplifiedcoding.simplifiedcoding.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import myapp.model.Employee;
import myapp.EmployeeInfoActivity;
import myapp.ProductInfoActivity;
import myapp.api.CommonApi;
import myapp.api.URLs;
import myapp.model.Product;

public class AsyncTaskApi implements CommonApi {

    private Activity mActivity;

    private Object mObject;
    private List<Object> mObjects;

    public AsyncTaskApi(Activity activity) {
        mActivity = activity;
    }

    public Object getObject() {
        return mObject;
    }

    public List<Object> getObjects() {
        return mObjects;
    }

    @Override
    public Product getProduct(String symbol) {


        class ProductLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;
            private String mSymbol;

            public ProductLogin(String symbol) {
                mSymbol = symbol;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressBar = (ProgressBar) findViewById(R.id.progressBar);
                //progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(mActivity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the product from the response
                        JSONObject productJson = obj.getJSONObject("object");

                        //creating a product object
                        Product product = new Product(
                                productJson.getInt("id"),
                                productJson.getInt("quantity"),
                                productJson.getString("productname"),
                                productJson.getString("productsymbol")
                        );

                        //storing the product in shared preferences
                        SharedPrefManager.getInstance(mActivity.getApplicationContext()).productLogin(product);
                        mObject = product;

                        //starting the profile activity
                        mActivity.finish();
                        mActivity.startActivity(new Intent(mActivity.getApplicationContext(), ProductInfoActivity.class));
                    } else {
                        Toast.makeText(mActivity.getApplicationContext(), "Product does not exist in warehouse database", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("symbol", mSymbol);
                //params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_GET_PRODUCT, params);
            }
        }

        ProductLogin ul = new ProductLogin(symbol);
        ul.execute();
        return (Product)mObject;
    }

    @Override
    public Employee getEmployee(String symbol) {


        class EmployeeLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;
            private String mSymbol;

            public EmployeeLogin(String symbol) {
                mSymbol = symbol;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressBar = (ProgressBar) findViewById(R.id.progressBar);
                //progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    mObject = obj;

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(mActivity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the product from the response
                        JSONObject employeeJson = obj.getJSONObject("object");

                        //creating a product object
                        Employee employee = new Employee(
                                employeeJson.getInt("id"),
                                employeeJson.getString("surname"),
                                employeeJson.getString("name"),
                                employeeJson.getString("symbol")
                        );

                        //storing the employee in shared preferences
                        SharedPrefManager.getInstance(mActivity.getApplicationContext()).employeeLogin(employee);
                        mObject = employee;
                        //starting the profile activity
                        mActivity.finish();
                        mActivity.startActivity(new Intent(mActivity.getApplicationContext(), EmployeeInfoActivity.class));
                    } else {
                        Toast.makeText(mActivity.getApplicationContext(), "Employee does not exist in the database", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                //params.put("symbol", symbol);
                //params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_GET_EMPLOYEE, params);
            }
        }

        EmployeeLogin ul = new EmployeeLogin(symbol);
        ul.execute();
        return (Employee)mObject;
    }
}
