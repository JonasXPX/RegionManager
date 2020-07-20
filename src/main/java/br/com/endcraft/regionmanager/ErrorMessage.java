package br.com.endcraft.regionmanager;

import java.util.HashMap;
import java.util.Optional;

public class ErrorMessage {

    private static ErrorMessage instance;
    private static final long ERROR_TIMEOUT = 250;
    private HashMap<Long, String> errors = new HashMap<>();

    public static ErrorMessage getInstance() {
        if(instance == null) instance = new ErrorMessage();
        return instance;
    }

    public void addErrorMessage(String errorMessage) {
        if(hasError()) {
            errors.remove(System.currentTimeMillis() / ERROR_TIMEOUT);
        }
        errors.put(System.currentTimeMillis() / ERROR_TIMEOUT, errorMessage);
    }

    public boolean hasError() {
        return errors.containsKey(System.currentTimeMillis() / ERROR_TIMEOUT);
    }

    public String getError() {
        return Optional.of(errors.get(System.currentTimeMillis() / ERROR_TIMEOUT)).orElse("");
    }


}
