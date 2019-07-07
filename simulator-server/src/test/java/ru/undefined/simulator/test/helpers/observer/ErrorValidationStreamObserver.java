package ru.undefined.simulator.test.helpers.observer;

import io.grpc.stub.StreamObserver;
import ru.undefined.simulator.model.request.ResponseStatus;
import ru.undefined.simulator.model.request.SimpleResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ErrorValidationStreamObserver implements StreamObserver<SimpleResponse> {

    private String expectedMessage;
    private ResponseStatus expectedStatus;
    private boolean passed = false;

    public ErrorValidationStreamObserver(String expectedMessage, ResponseStatus expectedStatus) {
        this.expectedMessage = expectedMessage;
        this.expectedStatus = expectedStatus;
    }

    @Override
    public void onNext(SimpleResponse value) {
        assertEquals(expectedMessage, value.getMessage());
        assertEquals(expectedStatus, value.getStatus());
        passed = true;
    }

    @Override
    public void onError(Throwable t) {
        throw new RuntimeException(t);
    }

    @Override
    public void onCompleted() {
        assertTrue(passed);
    }
}
