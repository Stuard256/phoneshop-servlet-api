package com.es.phoneshop.model.product.service;

public class ArrayListProductService implements ProductService {

    public boolean containsWords(String description, String query) {
        query.trim().toLowerCase();
        description.trim().toLowerCase();
        String[] wordsInQuery = query.split(" ");
        String[] wordsInDescription = description.split(" ");
        for (String wordInQuery : wordsInQuery) {
            for (String wordInDescription : wordsInDescription) {
                if (wordInDescription.contains(wordInQuery)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int matchWords(String description, String query) {
        int result = 0;
        query = query.trim().toLowerCase();
        description = description.trim().toLowerCase();
        String[] wordsInQuery = query.split(" ");
        String[] wordsInDescription = description.split(" ");
        for (String wordInQuery : wordsInQuery) {
            for (String wordInDescription : wordsInDescription) {
                if (wordInDescription.equals(wordInQuery)) {
                    result++;
                    break;
                }
            }
        }
        return result;
    }
}
