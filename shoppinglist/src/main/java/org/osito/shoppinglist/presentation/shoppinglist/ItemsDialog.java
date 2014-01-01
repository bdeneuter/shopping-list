package org.osito.shoppinglist.presentation.shoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import org.osito.shoppinglist.R;

import static android.view.LayoutInflater.from;

public class ItemsDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private ItemsFragment fragment;

    public void setFragment(ItemsFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                              .setTitle(R.string.add_items)
                              .setView(from(getActivity()).inflate(R.layout.add_dialog, null))
                              .setCancelable(true)
                              .setPositiveButton(R.string.add, this)
                              .create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        EditText editText = getEditText();
        fragment.addItems(editText.getText().toString());
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }

    private EditText getEditText() {
        return (EditText) getDialog().findViewById(R.id.shops);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

}
