package com.thoughtworks.sulabh.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;

import java.util.ArrayList;

public class LooDetailsPopup {
    private AddLooActivity addLooActivity;
    private UpdateLooActivity updateLooActivity;
    CharSequence[] suitableOptions;
    private ArrayList<CharSequence> selectedCategories;
    private Button suitableFor;

    public LooDetailsPopup(AddLooActivity addLooActivity) {
        this.addLooActivity = addLooActivity;
    }

    public LooDetailsPopup(UpdateLooActivity updateLooActivity) {
        this.updateLooActivity = updateLooActivity;
    }

    protected void onChangeSelectedCategories() {
        suitableFor.setText(selectedCategories.get(0).toString()+" ...");
    }

    protected void showSelectCategoriesDialog() {
        if (addLooActivity!=null) {
            suitableFor = addLooActivity.getSuitableFor();
            suitableOptions = addLooActivity.getSuitableOptions();
            selectedCategories = addLooActivity.getSelectedCategories();
        }
        else {
            suitableFor = updateLooActivity.getSuitableFor();
            suitableOptions = updateLooActivity.getSuitableOptions();
            selectedCategories = updateLooActivity.getSelectedCategories();
        }

        boolean[] checkedCategories = new boolean[suitableOptions.length];
        int count = suitableOptions.length;

        for (int i = 0; i < count; i++)
            checkedCategories[i] = selectedCategories.contains(suitableOptions[i]);

        DialogInterface.OnMultiChoiceClickListener categoriesDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked)
                    selectedCategories.add(suitableOptions[which]);
                else
                    selectedCategories.remove(suitableOptions[which]);
                onChangeSelectedCategories();
            }
        };

        AlertDialog.Builder builder;
        if (addLooActivity!=null)
            builder= new AlertDialog.Builder(addLooActivity);
        else
            builder= new AlertDialog.Builder(updateLooActivity);
        builder.setTitle("Select Category");
        builder.setMultiChoiceItems(suitableOptions, checkedCategories, categoriesDialogListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}