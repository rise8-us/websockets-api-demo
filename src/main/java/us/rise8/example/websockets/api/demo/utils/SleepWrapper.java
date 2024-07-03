package us.rise8.example.websockets.api.demo.utils;

import org.springframework.stereotype.Service;

@Service
public class SleepWrapper {

    public void sleep(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }
}
