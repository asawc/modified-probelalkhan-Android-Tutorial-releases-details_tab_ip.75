package myapp.releaseActivity;

//import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.MainThread;

import net.simplifiedcoding.simplifiedcoding.SharedPrefManager;

import myapp.ScanToCheckActivity;
import myapp.model.Product;

public class ScanToAddActivity extends ScanToCheckActivity {

    private final String ADD_EMPLOYEE_TAG = "ADD_PRODUCT";
    private final String ADD_PRODUCT_TAG = "ADD_PRODUCT";


    public ScanToAddActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Pobranie z poprzedniej aktywności, jakiego typu ma być operacja,
        // tzn czy dodać produkt, czy pracownika
        mScanType = ScanType.fromId(
                getIntent().getIntExtra(ScanType.class.getName(),
                        ScanType.None.ordinal()));

        savedInstanceState = savedInstanceState == null ? new Bundle() : savedInstanceState;
        savedInstanceState.putBoolean(IS_NEXT_ACTIVITY_KEY, false);
        savedInstanceState.putInt(ScanType.class.getName(), mScanType.getId());
        super.onCreate(savedInstanceState);

        if(mScanType == ScanType.Product)
            mButtonCheck.setText("Add product");

        if(mScanType == ScanType.Employee)
            mButtonCheck.setText("Add employee");


        Product p= SharedPrefManager.getInstance(getApplicationContext()).getProduct();
        Log.d(ScanToAddActivity.class.getName(), "symbol"+(p!=null? p.getSymbol() :null) );
        //Log.d(ScanToAddActivity.class.getName(), "mScannedProduct="+mProductScanned != null ?
         //       mProductScanned.getSymbol() : "None");
    }

    /*
    public void getProduct () {
        Call<ResponseContainer<Product>> callProducts = mApiService.getProduct(symbol);
        callProducts.enqueue(new CallbackImpl<ResponseContainer<Product>>(GET_PRODUCT_TAG){
            @Override
            public void onResponse(Call<ResponseContainer<Product>> call,
                                   Response<ResponseContainer<Product>> response) {
                super.onResponse(call, response);
                ResponseContainer<Product> resCon = response.body();
                Log.d(getTag(), resCon.getMessage());

                productScanned = resCon.getObject();
                Product prod = new Product(productScanned.getId(), productScanned.getQuantity(),
                        productScanned.getName(), productScanned.getSymbol());
                String strProd = prod.getSymbol()+ "  " + prod.getName() + "  " +
                        String.valueOf(prod.getQuantity());

            }
        });
    }

    @Override
    protected void setButtonCheckAction() {
        mButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // mRetrofitApiSevice.addProduct);
                // employeeLogin();
                // productLogin();
            }
        });
    }

    public void addEmployee () {

    }

/*
    // public void checkItemInWarehouse() {
    //Intent intent = new Intent(this, ProductInfoActivity.class);
    // startActivity(intent);
    private void productLogin() {


        class ProductLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

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
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

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
                        SharedPrefManager.getInstance(getApplicationContext()).productLogin(product);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProductInfoActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Product does not exist in warehouse database", Toast.LENGTH_SHORT).show();
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
                params.put("productsymbol", symbol);
                //params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_GET_PRODUCT, params);
            }
        }

        ProductLogin ul = new ProductLogin();
        ul.execute();
    }

    private void employeeLogin() {


        class EmployeeLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

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
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the product from the response
                        JSONObject employeeJson = obj.getJSONObject("employee");

                        //creating a product object
                        Employee employee = new Employee(
                                employeeJson.getInt("id"),
                                employeeJson.getString("surname"),
                                employeeJson.getString("name"),
                                employeeJson.getString("symbol")
                        );

                        //storing the employee in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).employeeLogin(employee);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), EmployeeInfoActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Employee does not exist in the database", Toast.LENGTH_SHORT).show();
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
                return requestHandler.sendPostRequest(URLs.URL_GET_EMPLOYEE, params);
            }
        }

        EmployeeLogin ul = new EmployeeLogin();
        ul.execute();
    }
*/
}
