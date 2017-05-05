package com.bogueratcreations.eaftoolkit.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by jodyroth on 3/10/17.
 */

public class ShowDialog extends DialogFragment {

    public static final String ARG_TITLE = "ShowDialog.Title";
    public static final String ARG_MSG = "ShowDialog.Message";

    public ShowDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString(ARG_TITLE);
        String message = args.getString(ARG_MSG);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
                    }
                })
                .create();
    }

    // ***************
    // Call it using the following
    // ***************

    //    DialogFragment dialog = new ShowDialog();
    //    Bundle args = new Bundle();
    //    args.putString(ShowDialog.ARG_TITLE, title);
    //    args.putString(ShowDialog.ARG_MESSAGE, message);
    //    dialog.setArguments(args);
    //    dialog.setTargetFragment(this, YES_NO_CALL);
    //    dialog.show(getFragmentManager(), "tag");



//    /* The activity that creates an instance of this dialog fragment must
// * implement this interface in order to receive event callbacks.
// * Each method passes the DialogFragment in case the host needs to query it. */
//    public interface saveDialogListener {
//        void onDialogPositiveClick(DialogFragment dialog);
//        void onDialogNegativeClick(DialogFragment dialog);
//    }
//
//    // Use this instance of the interface to deliver action events
//    saveDialogListener mListener;
//
//    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (saveDialogListener) activity;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(activity.toString()
//                    + " must implement saveDialogListener");
//        }
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Save Changes?")
//                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // Send the event back to the host activity.
//                        mListener.onDialogPositiveClick(ShowDialog.this);
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // Send the event back to the host activity.
//                        mListener.onDialogNegativeClick(ShowDialog.this);
//                    }
//                });
//        // Create the dialog and return it
//        return builder.create();
//    }
}
