package com.example.izobonga_waiting_app.tts;

//플레이어의 재생상태를 나타내기 위한 enum 클래스
//state 에 엉뚱한 값이 할당되는 것을 방지
public enum PlayState {
    PLAY("재생 중"), WAIT("일시정지"), STOP("멈춤");

    private String state;

    PlayState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public boolean isStopping() {
        return this == STOP;
    }

    public boolean isWaiting() {
        return this == WAIT;
    }

    public boolean isPlaying() {
        return this == PLAY;
    }
}
