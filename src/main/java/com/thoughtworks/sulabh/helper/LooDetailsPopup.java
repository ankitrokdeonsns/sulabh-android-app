package com.thoughtworks.sulabh.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;
import com.thoughtworks.sulabh.activity.AddLooActivity;
import com.thoughtworks.sulabh.activity.UpdateLooActivity;

import java.util.ArrayList;

public class LooDetailsPopup {
    private AddLooActivity addLooActivity;
    private UpdateLooActivity updateLooActivity;
    CharSequence[] allSuitableOptions;
    private ArrayList<CharSequence> currentSelectedCategories;
    private Button suitableForButton;
    private String previouslySelectedCategories;
    private String[] previousSelectedOptions;
    private boolean[] checkedCategories;

    public LooDetailsPopup(AddLooActivity addLooActivity) {
        this.addLooActivity = addLooActivity;
    }

    public LooDetailsPopup(UpdateLooActivity updateLooActivity) {
        this.updateLooActivity = updateLooActivity;
    }


    protected void onChangeSelectedCategories() {
        StringBuilder categories = new StringBuilder();
        for (CharSequence selectedCategory : currentSelectedCategories) {
            categories.append(selectedCategory).append("\n");
        }
        suitableForButton.setText(categories);
    }

    public void showSelectCategoriesDialog() {
        if (addLooActivity!=null) {
            suitableForButton = addLooActivity.getSuitableForButton();
            allSuitableOptions = addLooActivity.getAllSuitableOptions();
            currentSelectedCategories = addLooActivity.getSelectedCategories();
        }
        else {
            suitableForButton = updateLooActivity.getSuitableForButton();
            allSuitableOptions = updateLooActivity.getAllSuitableOptions();
            currentSelectedCategories = updateLooActivity.getSelectedCategories();
            previouslySelectedCategories = updateLooActivity.getSelectedLooSuitableFor();
        }

        checkedCategories = new boolean[allSuitableOptions.length];


        if(updateLooActivity!= null){
            previousSelectedOptions = previouslySelectedCategories.split("\n");
            for (String previousSelectedOption : previousSelectedOptions) {
                if(!currentSelectedCategories.contains(previousSelectedOption))
                    currentSelectedCategories.add(previousSelectedOption);
            }
        }



        DialogInterface.OnMultiChoiceClickListener categoriesDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(updateLooActivity!=null) {
                    for (String previousSelectedOption : previousSelectedOptions) {
                        if (!currentSelectedCategories.contains(previousSelectedOption))
                            currentSelectedCategories.add(previousSelectedOption);
                    }
                }
                if (isChecked) {
                    if(!currentSelectedCategories.contains(allSuitableOptions[which]))
                        currentSelectedCategories.add(allSuitableOptions[which]);
                }
                else
                    currentSelectedCategories.remove(allSuitableOptions[which]);

                onChangeSelectedCategories();
            }

        };


        for (int i = 0; i < allSuitableOptions.length; i++) {
            checkedCategories[i] = currentSelectedCategories.contains(allSuitableOptions[i]);
        }

        AlertDialog.Builder builder;
        if (addLooActivity!=null)
            builder= new AlertDialog.Builder(addLooActivity);
        else
            builder= new AlertDialog.Builder(updateLooActivity);
        builder.setTitle("Select Category");
        builder.setMultiChoiceItems(allSuitableOptions, checkedCategories, categoriesDialogListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}