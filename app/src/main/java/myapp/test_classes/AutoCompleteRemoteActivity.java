package myapp.test_classes;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import net.simplifiedcoding.simplifiedcoding.R;

public class AutoCompleteRemoteActivity extends AppCompatActivity {
    private AutoCompleteTextView storeEmpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_employee);

        //gets remote data asynchronously and adds it to AutoCompleteTextView
        RemoteData remoteData = new RemoteData(this);
        remoteData.getStoreData();

        storeEmpl = (AutoCompleteTextView)findViewById(R.id.autoComplete);
        storeEmpl.setOnItemClickListener(onItemClickListener);
    }

    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Toast.makeText(AutoCompleteRemoteActivity.this,
                            "Clicked item "
                                    + adapterView.getItemAtPosition(i)
                            , Toast.LENGTH_SHORT).show();
                }
            };
}