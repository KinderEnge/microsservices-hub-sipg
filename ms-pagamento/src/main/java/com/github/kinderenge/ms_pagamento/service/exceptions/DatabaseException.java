package com.github.kinderenge.ms_pagamento.service.exceptions;

public class DatabaseException extends RuntimeException{
    public DatabaseException(String message) {
        super(message);
    }
}