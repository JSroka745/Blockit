package com.example.myapp.ui.zablokowane;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ZablkowaneViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ZablkowaneViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}