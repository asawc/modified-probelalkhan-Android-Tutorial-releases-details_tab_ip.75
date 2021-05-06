package myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import net.simplifiedcoding.simplifiedcoding.R;
import net.simplifiedcoding.simplifiedcoding.SharedPrefManager;

import myapp.api.ApiBuilder;
import myapp.api.ApiServiceType;
import myapp.api.AsyncTask.AsyncTaskApi;
import myapp.api.RetrofitApi;

public abstract class ScanActivity extends AppCompatActivity {

    protected CodeScanner mCodeScanner;
    protected CodeScannerView mScannerView;
    protected Button mButtonCheck, mButtonLogout;

    protected String mSymbol;
    protected ScanType mScanType;

    protected final ApiServiceType mApiServiceType;

    protected RetrofitApi mRetrofitApiSevice;
    protected AsyncTaskApi mAsyncTaskApiService;

    public ScanActivity() {
        mScanType = ScanType.EACH;
        // Choose ApiServiceType
        mApiServiceType = ApiServiceType.Retrofit;

        if(mApiServiceType == ApiServiceType.Retrofit)
            mRetrofitApiSevice = new ApiBuilder().getApiService();

        if(mApiServiceType == ApiServiceType.AsyncTask )
            mAsyncTaskApiService = new AsyncTaskApi(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mScannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, mScannerView);

        mButtonCheck = (Button) findViewById(R.id.buttonCheck);
        mButtonLogout = (Button) findViewById(R.id.buttonLogout2);

        setScannerAction();

        setButtonCheckAction();

        setButtonLogoutAction();
    }

    protected void setButtonCheckAction() {

    }

    protected void setButtonLogoutAction() {
        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
    }

    protected void setScannerAction() {
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(ScanActivity.this, result.getText(), Toast.LENGTH_SHORT); //.show();
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 330);
                        toast.show();
                        mSymbol = result.getText();
                    }
                });
            }
        });

        mCodeScanner.startPreview();

        mScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    public enum ScanType { Product(1), Employee(2), None(3), EACH(4);
        private final int id; // Could be other data type besides int
        ScanType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static ScanType fromId(int id) {
            for (ScanType type : values()) {
                if (type.getId() == id) {
                    return type;
                }
            }
            return null;
        }
    }
}