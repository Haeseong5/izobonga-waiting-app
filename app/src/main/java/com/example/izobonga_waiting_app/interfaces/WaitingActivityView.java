package com.example.izobonga_waiting_app.interfaces;

public interface WaitingActivityView {
    void validateSuccess(String message, int ticket);

    void validateFailure(String message);
}
