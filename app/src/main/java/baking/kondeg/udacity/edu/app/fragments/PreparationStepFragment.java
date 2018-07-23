package baking.kondeg.udacity.edu.app.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import baking.kondeg.udacity.edu.app.R;
import baking.kondeg.udacity.edu.app.activities.DetailActivity;
import baking.kondeg.udacity.edu.app.data.PreparationInstruction;
import baking.kondeg.udacity.edu.app.data.Recipe;
import baking.kondeg.udacity.edu.app.utils.EndpointValues;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PreparationStepFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreparationStepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreparationStepFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String LOG_TAG = PreparationStepFragment.class.getSimpleName();

    private ListItemClickListener mListener;
    private Recipe selectedRecipe;
    private Integer clickedIndex;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    Uri mediaUri;

    public PreparationStepFragment() {
        // Required empty public constructor
    }

    public interface ListItemClickListener {
        void onListItemClick(List<PreparationInstruction> allSteps, int Index);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreparationStepFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreparationStepFragment newInstance(String param1, String param2) {
        PreparationStepFragment fragment = new PreparationStepFragment();
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
        mListener = (DetailActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        if(savedInstanceState != null) {
            selectedRecipe = savedInstanceState.getParcelable(EndpointValues.selectedRecipe);
            clickedIndex = savedInstanceState.getInt(EndpointValues.itemClicked);
        } else {
            selectedRecipe = getArguments().getParcelable(EndpointValues.selectedRecipe);
            clickedIndex = getArguments().getInt(EndpointValues.itemClicked);

            Log.d(LOG_TAG," restoreInstanceState");
            Log.d(LOG_TAG, selectedRecipe.getInstructions().get(clickedIndex).getDescription());
            Log.d(LOG_TAG, clickedIndex.toString());
        }

        View view = inflater.inflate(R.layout.fragment_preparation_step, container, false);

        TextView textView = (TextView) view.findViewById(R.id.recipe_step_detail_text);
        if (textView!=null) {
            if (textView.getTag() != null && textView.getTag().equals("phone-land")) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                View decorView = getActivity().getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
            textView.setText(selectedRecipe.getInstructions().get(clickedIndex).getDescription());
            textView.setVisibility(View.VISIBLE);
        }
        playerView = (PlayerView) view.findViewById(R.id.playerView);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        String imageUrl=selectedRecipe.getInstructions().get(clickedIndex).getThumbnailURL();
        if (imageUrl!=null) {
            ImageView thumbImage = (ImageView) view.findViewById(R.id.thumbImage);
            Picasso.with(getContext()).load(Uri.parse(imageUrl).buildUpon().build()).into(thumbImage);
        }

        String videoURL = selectedRecipe.getInstructions().get(clickedIndex).getVideoURL();
        mediaUri = Uri.parse(videoURL);
        if (videoURL!=null && !videoURL.isEmpty()) {
            playerView.setVisibility(View.VISIBLE);
            playerSetup(mediaUri);
            if (textView.getTag()!=null && textView.getTag().equals("tablet-land")) {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);

            }
            else if (textView.getTag()!=null && textView.getTag().equals("phone-land") && isInLandscapeMode(getContext())){
                textView.setVisibility(View.GONE);
            }
        }
        else {
            player=null;
            playerView.setVisibility(View.GONE);
        }
        Button mPrevStep = (Button) view.findViewById(R.id.previousStep);
        Button mNextstep = (Button) view.findViewById(R.id.nextStep);
        mPrevStep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (clickedIndex>0) {
                    if (player!=null){
                        player.stop();
                    }
                    clickedIndex = clickedIndex - 1;
                    mListener.onListItemClick(selectedRecipe.getInstructions(),clickedIndex);
                }
                else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.first_step), Toast.LENGTH_SHORT).show();

                }
            }});

        mNextstep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int lastIndex = selectedRecipe.getInstructions().size()-1;
                if (clickedIndex<lastIndex) {
                    if (player!=null){
                        player.stop();
                    }
                    clickedIndex = clickedIndex + 1;
                    mListener.onListItemClick(selectedRecipe.getInstructions(),clickedIndex);
                }
                else {
                    Toast.makeText(getContext(),getResources().getString(R.string.last_step), Toast.LENGTH_SHORT).show();

                }
            }});
        return view;
    }

    public boolean isInLandscapeMode( Context context ) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    private void playerSetup(Uri mediaUri) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            playerView.setPlayer(player);

            player.setPlayWhenReady(true);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory(getResources().getString(R.string.app_name))).
                    createMediaSource(mediaUri);
            player.prepare(mediaSource, true, false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        Log.d(LOG_TAG,"onSaveInstanceStateFragment");
        currentState.putParcelable(EndpointValues.selectedRecipe,selectedRecipe);
        currentState.putInt(EndpointValues.itemClicked,clickedIndex);
        super.onSaveInstanceState(currentState);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            playerSetup(mediaUri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            playerSetup(mediaUri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        releasePlayer();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
