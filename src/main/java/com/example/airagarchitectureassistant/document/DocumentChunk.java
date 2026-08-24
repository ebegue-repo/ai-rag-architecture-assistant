package com.example.airagarchitectureassistant.document;

import java.util.List;

public record DocumentChunk(
        int index,
        String text,
        List<String> headingPath,
        List<Integer> headingLevels,
        String sectionTitle,
        int startLineNumber,
        int endLineNumber) {
}
