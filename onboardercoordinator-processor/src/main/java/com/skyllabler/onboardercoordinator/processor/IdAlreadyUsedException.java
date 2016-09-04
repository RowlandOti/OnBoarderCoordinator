package com.skyllabler.onboardercoordinator.processor;

public class IdAlreadyUsedException extends Throwable {

    private FactoryAnnotatedClass existing;

    public IdAlreadyUsedException(FactoryAnnotatedClass existing) {
        this.existing = existing;
    }

    public FactoryAnnotatedClass getExisting() {
        return existing;
    }
}
