package com.iasdietideals24.backend.mappers;

public interface Mapper<A,B> {

    B mapTo(A a);

    A mapFrom(B b);

}