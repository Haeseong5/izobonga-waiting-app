package com.example.izobonga_waiting_app.interfaces;

import com.example.izobonga_waiting_app.model.WaitingData;

public interface WaitingActivityView {
    void validateSuccess(String message, int ticket);

    void validateFailure(String message);

    void modified(WaitingData waitingData);

}
