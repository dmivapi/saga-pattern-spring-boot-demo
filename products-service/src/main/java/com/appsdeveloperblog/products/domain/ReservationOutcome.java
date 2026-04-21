package com.appsdeveloperblog.products.domain;

public sealed interface ReservationOutcome permits Reserved, InsufficientStock, ProductNotFound {}
