package com.tutorconnect.app.model;

import java.util.List;

public class Dictionary {
    private String partOfSpeech;
    private List<String> definitions;

    // Getter and Setter for partOfSpeech
    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    // Getter and Setter for definitions
    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    public Dictionary(String partsOfSpeech, List<String> definitions) {
        this.partOfSpeech = partsOfSpeech;
        this.definitions = definitions;
    }
}
