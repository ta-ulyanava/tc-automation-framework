package com.example.teamcity.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Fields annotated with this will be filled with the provided value, if a parameter is passed.
 */
public @interface Parameterizable {
}
