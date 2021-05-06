package myapp.releaseActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.simplifiedcoding.simplifiedcoding.R;

import java.util.ArrayList;
import java.util.List;

import myapp.model.ProductRelease;
import myapp.model.ProductStatus;
import myapp.model.Release;
import myapp.model.ReleaseStatus;
import myapp.modelView.ReleaseProductsView;
import myapp.modelView.ReleaseView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsReleaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsReleaseFragment extends Fragment {

    private final String FRAGMENT_TAG = "RELEASE_PRODUCTS_INFO";

    protected List<ProductRelease> mReleaseProducts;

    protected RecyclerView mRecyclerView;
    protected ReleaseProductsAdapter mRelProdsAdapter;
    protected List<ReleaseProductsView> mRelProductsView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailsReleaseFragment() {
        // Required empty public constructor
        mReleaseProducts = new ArrayList<ProductRelease>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsReleaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsReleaseFragment newInstance(String param1, String param2) {
        DetailsReleaseFragment fragment = new DetailsReleaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.fragment_details_release, container, false);
        mRecyclerView = fragmentLayout.findViewById(R.id.relProductsRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mReleaseProducts = new ArrayList<ProductRelease>();
        mRelProductsView = new ArrayList<ReleaseProductsView>();
        mReleaseProducts = ((ReleaseInfoActivity) getActivity()).getRelease().getProductsRelease();

        //Log.d( "Status","Product order status: " + relProduct.getStatus() + " .");

        if(mReleaseProducts == null || mReleaseProducts.isEmpty())
            mRelProductsView.add(new ReleaseProductsView(0, ReleaseStatus.in_progress.name(),
                    "0", 0));
        else
            for(ProductRelease relProduct : mReleaseProducts)
                mRelProductsView.add(new ReleaseProductsView(relProduct.getProduct().getId(),
                        relProduct.getStatus().name(), relProduct.getProduct().getName(), relProduct.getQuantity()));

        mRelProdsAdapter = new ReleaseProductsAdapter(mRelProductsView);
        mRecyclerView.setAdapter(mRelProdsAdapter);
        // Inflate the layout for this fragment
        return fragmentLayout;
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        mReleaseProducts = ((ReleaseInfoActivity) getActivity()).getRelease().getProductsRelease();

        //Log.d( "Status","Product order status: " + relProduct.getStatus() + " .");

        if(mReleaseProducts == null || mReleaseProducts.isEmpty())
            mRelProductsView.add(new ReleaseProductsView(0, ReleaseStatus.in_progress.name(),
                    "Automatyka i Robotyka", 56));
        else
            for(ProductRelease relProduct : mReleaseProducts)
                mRelProductsView.add(new ReleaseProductsView(relProduct.getProduct().getId(),
                        relProduct.getStatus().name(), relProduct.getProduct().getName(), relProduct.getQuantity()));

        mRelProdsAdapter.notifyDataSetChanged();
    }
*/
    public class ReleaseProductsAdapter extends RecyclerView.Adapter<ReleaseProductsAdapter.ViewHolder> {

        private static final String TAG = "ReleaseAdapter";

        private List<ReleaseProductsView> mRelProductsView;

        // VievHolder do przechowywania widoków elementów listy
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView mIdTextView;
            private final TextView mStatusTextView;
            private final TextView mReqQuantityTextView;
            private final TextView mNameTextView;

            public ViewHolder(View v) {
                super(v);

                mIdTextView = (TextView) v.findViewById(R.id.id_prod_order);            // mIdProdOrderTextView
                mNameTextView = (TextView) v.findViewById(R.id.prod_name);              // mProdNameTextView
                mReqQuantityTextView = (TextView) v.findViewById(R.id.prod_req_quant);
                mStatusTextView = (TextView) v.findViewById(R.id.prod_order_status);    // mStatusTextView

                // Define click listener for the ViewHolder's View.
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");


                    }
                });
            }



            public TextView getIdTextView() {
                return mIdTextView;
            }

            public TextView getStatusTextView() {
                return mStatusTextView;
            }

            public TextView getNameTextView() {
                return mNameTextView;
            }

            public TextView getReqQuantityTextView() {
                return mReqQuantityTextView;
            }

        }



        /**
         * Initialize the dataset of the Adapter.
         *
         * @param relView List<ReleaseView> containing the data to populate views to be used by RecyclerView.
         */
        public ReleaseProductsAdapter(List<ReleaseProductsView> relView) {
            mRelProductsView = relView;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public ReleaseProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_prod_orders, parent, false);
            return new ReleaseProductsAdapter.ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull ReleaseProductsAdapter.ViewHolder holder, int position) {
            Log.d(TAG, "Element " + position + " set.");
            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            holder.getIdTextView().setText(String.valueOf(mRelProductsView.get(position).getId()));
            holder.getReqQuantityTextView().setText(String.valueOf(mRelProductsView.get(position).getReqQuantity()));
            holder.getStatusTextView().setText(mRelProductsView.get(position).getStatus());
            holder.getNameTextView().setText(mRelProductsView.get(position).getName());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mRelProductsView == null ? 0 : mRelProductsView.size();
        }

        public List<ReleaseProductsView> getReleaseViews() {
            return mRelProductsView;
        }
    }
}