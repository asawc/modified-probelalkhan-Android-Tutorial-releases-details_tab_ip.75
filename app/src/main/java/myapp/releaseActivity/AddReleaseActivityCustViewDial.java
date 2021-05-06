package myapp.releaseActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import net.simplifiedcoding.simplifiedcoding.R;
import net.simplifiedcoding.simplifiedcoding.RequestHandler;
import net.simplifiedcoding.simplifiedcoding.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import myapp.ProductInfoActivity;
import myapp.ScanActivity;
import myapp.api.URLs;
import myapp.model.Product;
import myapp.api.ApiBuilder;
import myapp.api.CallbackImpl;
import myapp.api.RetrofitApi;
import myapp.api.ResponseContainer;
import myapp.model.Employee;
import myapp.model.ProductRelease;
import myapp.model.ProductStatus;
import myapp.model.Release;
import myapp.model.ReleaseStatus;
import myapp.modelView.EmployeeView;
import myapp.modelView.ProductInputView;
import myapp.modelView.ProductView;
import retrofit2.Call;
import retrofit2.Response;


public class AddReleaseActivityCustViewDial extends AppCompatActivity {

    private final String ADD_RELEASE_TAG = "ADD_RELEASE";

    private Integer emplPosition;
    private boolean emplChecked;
    private EmployeeAdapter<EmployeeView> employeesAdapter;
    private List<EmployeeView> employeeViews;
    private List<Employee> employeeList;
    private final String GET_EMPLOYEES_TAG = "GET_EMPLOYEES";

    private final String GET_PRODUCTS_TAG = "GET_PRODUCTS";
    private ProductInputAdapter<ProductInputView> productsInputAdapter;
    private List<ProductInputView> productViews;
    private List<Product> productList;
    private boolean[] checkedProducts;
    private CharSequence[] prodsInChars;
    private String symbol;

    private final RetrofitApi apiService;

    public AddReleaseActivityCustViewDial() {
        apiService = new ApiBuilder().getApiService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_add_activity);

        SharedPrefManager.getInstance(this).removeProduct();

        productViews = new ArrayList<ProductInputView>();
        productList = new ArrayList<Product>();

        emplChecked = false;
        emplPosition = null;
        employeeViews = new ArrayList<EmployeeView>();
        employeeList = new ArrayList<Employee>();

        final Button productsButton = (Button) findViewById(R.id.products_button);
        final Button productScanButton = (Button) findViewById(R.id.product_scan_button);
        final Button employeeButton = (Button) findViewById(R.id.employees_button);
        final Button addReleaseButton = (Button) findViewById(R.id.butonAddRelease);

        final ListView prodsListView = findViewById(R.id.products_list);

//*******// PRACOWNICY ******************************************************************
        // Pobranie pracowników
        getEmployees();

        employeesAdapter = new EmployeeAdapter(this, R.layout.employe_spinner_dropdown_item, employeeViews);
        employeesAdapter.setDropDownViewResource(R.layout.employe_spinner_dropdown_item);
        //Setting the EmployeeAdapter data on the AlertDialog
        employeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(view.getContext(), "Button Clicked", Toast.LENGTH_LONG).show();
                buildEmployeesAlertDialog().show();
            }
        });

//*******// PRODUKTY ******************************************************************* /
        //pobranie produktów
        getProducts();
        Log.d("ProductListSize", String.valueOf(productList.size()));

        productsInputAdapter = new ProductInputAdapter<ProductInputView>(this,
                        R.layout.release_add_activity_prod_list_item, productViews);
        //Setting the ProductInputAdapter data on the Prods List View
        prodsListView.setAdapter(productsInputAdapter);
        productsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildProductsAlertDialog().show();
            }
        });

        productScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanActivity();
            }
        });

        // jeżeli wybrany jest pracownik i przynajmniej jeden produkt
       addReleaseButton.setClickable(true);
       addReleaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("addReleaseButton", "Main Button Clicked");
                Employee emplSelected = null;
                List<ProductRelease> selectedProds = new ArrayList<>();

                TextView errTxtView = findViewById(R.id.input_data_error);
                errTxtView.setText("");
                if(emplChecked == false)
                    errTxtView.setText("Select an employee, please. \n");
                else
                    emplSelected = employeeList.get(emplPosition);

                if(productViews.isEmpty())
                    errTxtView.append("Select a product, please. \n");
                else if(productViews.isEmpty() == false) {
                    for(ProductInputView pView : productViews){
                        Integer reqQuantity = pView.getRequestedQuantity();
                        if(reqQuantity == null || reqQuantity <= 0){
                            errTxtView.append("Type requested quality on each product item. \n");
                            errTxtView.append("Requested quality must be higher than  0.");
                            productsInputAdapter.setIsConfirmButtonClicked(true);
                            break;
                        }
                        if(emplChecked) {
                            for (Product prod : productList) {
                                if (pView.getSymbol().equals(prod.getSymbol()))
                                    selectedProds.add(new ProductRelease(prod,
                                            ProductStatus.awaited, (reqQuantity == null ? 0 : reqQuantity)));
                            }
                        }
                    }
                    //if(errTxtView.getTag() != null)
                    //    errTxtView.append(errTxtView.getTag().toString());
                }
                Log.d("ErrTxtViewLength", String.valueOf(errTxtView.getText().length()));
                // jeżeli nie ma błędów do wyswietlenia
                // to zapisz wydanie i przejdź do nowej aktywności
                if(errTxtView.getText().length() == 0)  {
                    // emplSelected
                    // selectedProds
                    Release newRelease = new Release(-1, emplSelected, ReleaseStatus.in_progress, selectedProds,
                            null, null);
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(newRelease);
                    Log.d(ADD_RELEASE_TAG, jsonString);

                    Call<ResponseContainer<Release>> call = apiService.addRelease(newRelease);
                    call.enqueue(new CallbackImpl<ResponseContainer<Release>>(ADD_RELEASE_TAG) {
                            @Override
                            public void onResponse(Call<ResponseContainer<Release>> call,
                                Response<ResponseContainer<Release>> response){

                                ResponseContainer<Release> resCon = response.body();
                                Log.d(getTag()+"_TAG", resCon.getMessage());

                                Toast.makeText(getApplicationContext(), "Release was added to warehouse database.",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    );
                    openReleaseActivity();
                }
                errTxtView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Product p = SharedPrefManager.getInstance(this).getProduct();
        if(p != null) {
            Log.d("onResume", "name: "+p.getName()+", symbol: "+p.getSymbol());

            boolean isNewProduct = true;
            // szukanie zeskanowaego produktu na liście widoków produktów
            // jeżeli nie ma na liście produktu, to dodajemy do listy
            // w innym przypadku nie dodajemy do listy i wyświetlamy komunikat
            for (int i = 0; i < productViews.size(); i++) {
                if( productViews.get(i).getSymbol().equals(p.getSymbol()) ) {
                    isNewProduct = false;
                }
            }
            if(isNewProduct) {
                // zaznaczenie checkboxu w product alert dialog
                for (int i = 0; i < productList.size(); i++) {
                    if( productList.get(i).getSymbol().equals(p.getSymbol()))
                        checkedProducts[i] = true;
                }
                productViews.add(new ProductInputView(
                        p.getSymbol(), p.getName(), p.getQuantity(), null)
                );
                productsInputAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(getApplicationContext(), "Scanned Product is already chosen on the list.",
                        Toast.LENGTH_SHORT).show();
        }
    }

    public void openScanActivity() {
        Intent intent = new Intent(this, ScanToAddActivity.class);
        intent.putExtra(ScanActivity.ScanType.class.getName(),
            ScanActivity.ScanType.Product.getId());
        startActivity(intent);
    }

    public void openReleaseActivity() {
        Intent intent = new Intent(this, ReleaseActivity.class);
        startActivity(intent);
    }

    public AlertDialog buildProductsAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        TextView dialogHeaderView = (TextView) inflater.inflate(R.layout.release_add_activity_header_dialog, null);
        dialogHeaderView.setText("Select Products...");
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddReleaseActivityCustViewDial.this);
        alertBuilder.setCustomTitle(dialogHeaderView);
        alertBuilder.setMultiChoiceItems(prodsInChars, checkedProducts, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //Toast.makeText(alertBuilder.getContext(), "Item Clicked: "+ which +"\n"+
                //      "IsChecked "+ String.valueOf(isChecked), Toast.LENGTH_LONG).show();
                checkedProducts[which] = isChecked;
            }
        });
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Czyści listę produktów
                if(!productViews.isEmpty()) {
                    productViews.clear();
                    productsInputAdapter.notifyDataSetChanged();
                }

                // Tworzy nową listę produktów zaznaczonych
                for (int i=0; i < checkedProducts.length; i++ ) {
                    if(checkedProducts[i]) {
                        Product p = productList.get(i);
                        ProductInputView pView = new ProductInputView(p.getSymbol(), p.getName(), p.getQuantity(), null);
                        productViews.add(pView);
                    }
                }
                // zmiana w widoku listy (ListView)
                if (!productViews.isEmpty())
                    productsInputAdapter.notifyDataSetChanged();
                // po zmianie widoku listy nie wyświetlaj
                // błędów w EditText - Requested Quantity
                productsInputAdapter.setIsConfirmButtonClicked(false);
                dialog.dismiss();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i=0; i < checkedProducts.length; i++ ) {
                    checkedProducts[i] = false;
                    Product p = productList.get(i);
                    for(ProductView pView : productViews) {
                        if(pView.getSymbol().equals(p.getSymbol()))
                            checkedProducts[i] = true;
                    }
                }
                dialog.dismiss();
            }
        });
        return alertBuilder.create();
    }

    public AlertDialog buildEmployeesAlertDialog() {
        //final View dialogHeaderView = findViewById(R.id.header_dialog_employees_textview);
        LayoutInflater inflater = getLayoutInflater();
        View dialogHeaderView = inflater.inflate(R.layout.release_add_activity_header_dialog, null);
        return new AlertDialog.Builder(AddReleaseActivityCustViewDial.this)
                .setCustomTitle(dialogHeaderView)
                .setAdapter(employeesAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        emplChecked = true;
                        emplPosition = which;
                        String emplText = employeesAdapter.getItem(which).getSymbol()+"  "+
                                employeesAdapter.getItem(which).getName()+"  "+
                                employeesAdapter.getItem(which).getSurname();
                        Toast.makeText(employeesAdapter.getContext(), "Item Clicked: "+ which +"\n"+
                                emplText, Toast.LENGTH_LONG).show();
                        // Wyświetlenie wybranego pracownika
                                /* LayoutInflater inflater = getLayoutInflater();
                                View viewActivity = inflater.inflate(R.layout.release_add_activity_dialog, null).;
                                viewActivity.findViewById(R.id.employeeChecked_textView); */
                        TextView checkedEmplTextView = (TextView) findViewById(R.id.employeeChecked_textView);
                        checkedEmplTextView.setText(emplText);
                        checkedEmplTextView.setVisibility(View.VISIBLE);

                        dialog.dismiss();
                    }
                }).create();
    }

    public void getEmployees() {
        // wykonanie zapytania - pobranie pracowników
        Call<ResponseContainer<List<Employee>>> call = apiService.getEmployees();
        call.enqueue(new CallbackImpl<ResponseContainer<List<Employee>>>(GET_EMPLOYEES_TAG) {
            @Override
            public void onResponse(Call<ResponseContainer<List<Employee>>> call,
                                   Response<ResponseContainer<List<Employee>>> response) {
                super.onResponse(call, response);

                ResponseContainer<List<Employee>> resCon = response.body();
                Log.d(getTag(), resCon.getMessage());

                employeeList = resCon.getObject();
                for(Employee emp : employeeList) {
                    employeeViews.add(new EmployeeView(emp.getSymbol(), emp.getName(), emp.getSurname()));
                    Log.d(getTag(), emp.getSymbol()+ " " + emp.getName() + " " +
                            emp.getSurname());
                }
            }
        });
    }

    public void getProducts () {
        Call<ResponseContainer<List<Product>>> callProducts = apiService.getProducts();
        callProducts.enqueue(new CallbackImpl<ResponseContainer<List<Product>>>(GET_PRODUCTS_TAG){
            @Override
            public void onResponse(Call<ResponseContainer<List<Product>>> call,
                                   Response<ResponseContainer<List<Product>>> response) {
                super.onResponse(call, response);
                ResponseContainer<List<Product>> resCon = response.body();
                Log.d(getTag(), resCon.getMessage());

                productList = resCon.getObject();
                checkedProducts = new boolean[productList.size()];
                for(int i=0; i < productList.size(); i++){
                    checkedProducts[i] = false;
                }

                prodsInChars = new CharSequence[productList.size()];
                int i=0;
                for(Product prod : productList) {
                    String strProd = prod.getSymbol()+ "  " + prod.getName() + "  " +
                            String.valueOf(prod.getQuantity());
                    // productViews.add(new ProductView(prod.getSymbol(), prod.getName(), prod.getQuantity()));
                    //prodsInString.add(strProd);
                    prodsInChars[i]=strProd;
                    i++;
                    Log.d(getTag(), strProd);
                }
                Log.d("ProdsInCharsSize ", String.valueOf(prodsInChars.length));
            }
        });
    }

    public class ProductInputAdapter<T> extends ArrayAdapter<T> {

        private int mResource;
        private boolean isConfirmButtonClicked;

        public ProductInputAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
            super(context, resource, objects);
            mResource = resource;
            isConfirmButtonClicked = false;
        }

        public View getCustomView(final int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(mResource, parent, false);

            TextView symbol = (TextView) layout.findViewById(R.id.symbol);
            symbol.setText(productViews.get(position).getSymbol());

            TextView name = (TextView) layout.findViewById(R.id.name);
            name.setText(productViews.get(position).getName());

            TextView quantity = (TextView) layout.findViewById(R.id.quantity);
            quantity.setText(String.valueOf(productViews.get(position).getQuantity()));

            final EditText reqQuantity = (EditText) layout.findViewById(R.id.requested_quantity);
            reqQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        String text = v.getText().toString();
                        productViews.get(position).setRequestedQuantity(Integer.parseInt(text));
                        reqQuantity.setText(text);
                        return true;
                    }
                    return false;
                }
            });
            Integer intQuantity = productViews.get(position).getRequestedQuantity();
            if(isConfirmButtonClicked && (intQuantity==null || intQuantity<=0)) {
                reqQuantity.setError("Type requested quality");
                reqQuantity.requestFocus();
            }
            String text = (intQuantity!=null) ?
                    String.valueOf(intQuantity) : "";
            reqQuantity.setText(text);
            Log.d("EditText#"+ String.valueOf(position), text);
            return layout;
        }

        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public void setIsConfirmButtonClicked(boolean isClicked) {
            isConfirmButtonClicked = isClicked;
        }

        public boolean isConfirmButtonClicked() {
            return this.isConfirmButtonClicked;
        }

        private class EditTextWatcher implements TextWatcher {
            public String mTemp="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mTemp = mTemp + s.toString();
                // Log.d("EditTextChanged", s.toString());
            }
        }
    }

    public class EmployeeAdapter<T> extends ArrayAdapter<T> {

        public EmployeeAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
            super(context, resource, objects);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.employe_spinner_dropdown_item, parent, false);

            TextView symbol = (TextView) layout.findViewById(R.id.symbol);
            symbol.setText(employeeViews.get(position).getSymbol());

            TextView name = (TextView) layout.findViewById(R.id.name);
            name.setText(employeeViews.get(position).getName());

            TextView surname = (TextView) layout.findViewById(R.id.surname);
            surname.setText(employeeViews.get(position).getSurname());
/*
            if(position == 0) {
                employeeViews.add(position, new EmployeeView("", "", ""));
                symbol.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                surname.setVisibility(View.GONE);

                TextView headerTextView = (TextView) layout.findViewById(R.id.header_textview);
                headerTextView.setVisibility(View.VISIBLE);
            }
            */
            return layout;
        }

        // It gets a View that displays in the drop down popup the data at the specified position
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }

    public class ProductAdapter<T> extends ArrayAdapter<T> {

        public ProductAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
            super(context, resource, objects);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.product_spinner_dropdown_item, parent, false);

            TextView symbol = (TextView) layout.findViewById(R.id.symbol);
            symbol.setText(productViews.get(position).getSymbol());

            TextView name = (TextView) layout.findViewById(R.id.name);
            name.setText(productViews.get(position).getName());

            TextView quantity = (TextView) layout.findViewById(R.id.product_quantity);
            quantity.setText(String.valueOf(productViews.get(position).getQuantity()));

            return layout;
        }

        // It gets a View that displays in the drop down popup the data at the specified position
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }

}