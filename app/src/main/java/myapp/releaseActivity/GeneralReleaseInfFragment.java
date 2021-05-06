package myapp.releaseActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.simplifiedcoding.simplifiedcoding.R;

import myapp.model.Release;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneralReleaseInfFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralReleaseInfFragment extends Fragment {

    private final String FRAGMENT_TAG = "GEN_RELEASE_INFO_FRAG";

    private Release mRelease;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GeneralReleaseInfFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GeneralReleaseInfFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneralReleaseInfFragment newInstance(String param1, String param2) {
        GeneralReleaseInfFragment fragment = new GeneralReleaseInfFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
        //
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
        View view = inflater.inflate(R.layout.fragment_general_release_inf, container, false);
        view = populateView(view);
        // Inflate the layout for this fragment
        return view;
    }

    public View populateView(View view) {
        mRelease = ((ReleaseInfoActivity) getActivity()).getRelease();
        Log.d(FRAGMENT_TAG, String.valueOf(mRelease.getId()) + " " + mRelease.getStatus());

        TextView releaseIdTextView = view.findViewById(R.id.textViewReleaseId);
        releaseIdTextView.setText(String.valueOf(mRelease.getId()));

        TextView creationDateTextView = view.findViewById(R.id.textViewCreationDate);
        creationDateTextView.setText(mRelease.getCreationDateTime());

        TextView emplSurnameTextView = view.findViewById(R.id.textViewEmployeeSurname2);
        emplSurnameTextView.setText(mRelease.getEmployee().getSurname());

        TextView emplNameTextView = view.findViewById(R.id.textViewEmployeeName2);
        emplNameTextView.setText(mRelease.getEmployee().getName());

        TextView emplSymbolTextView = view.findViewById(R.id.textViewEmployeeSymbol);
        emplSymbolTextView.setText(mRelease.getEmployee().getSymbol());

        //
        TextView releaseStatusTextView = view.findViewById(R.id.textViewReleaseStatus);
        releaseStatusTextView.setText(mRelease.getStatus().toString());

        return view;
    }

    public interface GetReleaseInfo {
        public Release getRelease();
    }
}