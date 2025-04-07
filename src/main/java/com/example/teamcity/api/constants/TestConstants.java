package com.example.teamcity.api.constants;

public class TestConstants {

    public static final String ROOT_PROJECT_ID = "_Root";
    public static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+-={}[]:\\";
    public static final String LOCALIZATION_CHARACTERS = "äöüßéèñçøπдёж漢字日本語한글";

    public static final String XSS_PAYLOAD = "<script>alert('XSSd')</script>";
    public static final String SQL_INJECTION_PAYLOAD = "'; DROP TABLE projects; --";
}
