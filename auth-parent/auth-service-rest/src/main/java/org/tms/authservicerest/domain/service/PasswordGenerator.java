package org.tms.authservicerest.domain.service;

import java.util.*;

public class PasswordGenerator {

    private static final int DEFAULT_LENGTH = 8;
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";

    private List<String> characterCategories;
    private int length;

    private PasswordGenerator(List<String> characterCategories, int length) {
        this.characterCategories = new ArrayList<>(characterCategories);
        this.length = length;
    }

    public static PasswordGeneratorBuilder builder() {
        return new PasswordGeneratorBuilder();
    }

    public static class PasswordGeneratorBuilder {

        private final List<String> characterCategories = new ArrayList<>(4);
        private int length;

        public PasswordGeneratorBuilder lower() {
            characterCategories.add(LOWER);
            return this;
        }

        public PasswordGeneratorBuilder upper() {
            characterCategories.add(UPPER);
            return this;
        }

        public PasswordGeneratorBuilder digits() {
            characterCategories.add(DIGITS);
            return this;
        }

        public PasswordGeneratorBuilder punctuation() {
            characterCategories.add(PUNCTUATION);
            return this;
        }

        public PasswordGeneratorBuilder length(int length) {
            this.length = Math.max(length, DEFAULT_LENGTH);
            return this;
        }

        public PasswordGenerator build() {
            return new PasswordGenerator(characterCategories, length);
        }
    }

    public String generatePassword() {
        StringBuilder password = new StringBuilder(length);
        Random random = new Random(System.nanoTime());

        for (int i = 0; i < password.length(); i++) {
            String category = characterCategories.get(random.nextInt(4));
            char character = category.charAt(random.nextInt(category.length()));
            password.insert(i, character);
        }

        return password.toString();
    }
}