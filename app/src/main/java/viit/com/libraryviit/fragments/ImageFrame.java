package viit.com.libraryviit.fragments;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import viit.com.libraryviit.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFrame.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFrame#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFrame extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ImageFrame() {
        // Required empty public constructor
    }
    public String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url Parameter 1.

     * @return A new instance of fragment ImageFrame.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageFrame newInstance(String url) {
        ImageFrame fragment = new ImageFrame();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, url);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_frame, container, false);
        ImageView v = (ImageView) view.findViewById(R.id.cover);
        picassoLoader(getContext(),v, mParam1);
//        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                v.removeOnLayoutChangeListener(this);
//                int cx = getArguments().getInt(MOTION_X_ARG);
//                int cy = getArguments().getInt(MOTION_Y_ARG);
//                int width = getResources().getDimensionPixelSize(R.dimen.fragment_appointment_width);
//                int height = getResources().getDimensionPixelSize(R.dimen.fragment_appointment_height);
//
//                float finalRadius = Math.max(width, height) / 2 + Math.max(width - cx, height - cy);
//                Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);
//                anim.setDuration(500);
//                anim.start();
//            }
//        });
        return view;

    }

    public void picassoLoader(Context context, ImageView imageView, String url){
        try {
            Picasso.with(context)
                    .load(url)
                    //.resize(30,30)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        }catch (Exception e){
            Picasso.with(context)
                    .load("http://img.clipartall.com/boy-reading-a-book-clip-art-reading-book-clipart-436_500.png")
                    //.resize(30,30)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        }

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
