package org.osito.shoppinglist.presentation.shops;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import org.osito.shoppinglist.R;

import static android.view.LayoutInflater.from;

public class ShopsDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private ShopsFragment fragment;

    public void setFragment(ShopsFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                              .setTitle(R.string.add_shops)
                              .setView(from(getActivity()).inflate(R.layout.add_dialog, null))
                              .setCancelable(true)
                              .setPositiveButton(R.string.add, this)
                              .create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        EditText editText = getEditText();
        fragment.addShops(editText.getText().toString());
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

}
