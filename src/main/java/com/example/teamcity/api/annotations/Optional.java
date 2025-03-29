package com.example.teamcity.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Fields marked with this annotation will be excluded from random or parameterized generation.
 * Values must be set manually (e.g., via setter).
 */
public @interface Optional {
}
