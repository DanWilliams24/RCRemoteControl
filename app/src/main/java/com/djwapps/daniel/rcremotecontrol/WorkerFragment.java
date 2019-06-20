package com.djwapps.daniel.rcremotecontrol;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class WorkerFragment extends Fragment {
    private Networker dataObject;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }


    public void setDataObject(Networker dataObject) {
        this.dataObject = dataObject;
    }


    public Networker getDataObject() {
        return dataObject;
    }
}
