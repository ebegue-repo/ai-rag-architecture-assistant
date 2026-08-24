package com.example.airagarchitectureassistant.document;

import java.util.ArrayList;
import java.util.List;

public class DocumentSection {

    private final List<String> headingPath;
    private final List<Integer> headingLevels;
    private final String sectionTitle;
    private int startLineNumber;
    private int endLineNumber;
    private final StringBuilder content = new StringBuilder();

    public DocumentSection(List<String> headingPath, List<Integer> headingLevels, String sectionTitle,
            int startLineNumber, int endLineNumber) {
        this.headingPath = headingPath;
        this.headingLevels = headingLevels;
        this.sectionTitle = sectionTitle;
        this.startLineNumber = startLineNumber;
        this.endLineNumber = endLineNumber;
    }

    public List<String> getHeadingPath() {
        return headingPath;
    }

    public List<Integer> getHeadingLevels() {
        return headingLevels;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public int getStartLineNumber() {
        return startLineNumber;
    }

    public int getEndLineNumber() {
        return endLineNumber;
    }

    public void appendContent(String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (content.length() > 0) {
            content.append(System.lineSeparator());
        }
        content.append(value);
    }

    public List<DocumentChunk> toChunks(ChunkingOptions chunkingOptions, int startingIndex) {
        String headingBlock = renderHeadingBlock();
        String bodyText = content.toString();
        if (bodyText.isBlank()) {
            DocumentChunk chunk = new DocumentChunk(
                    startingIndex,
                    headingBlock.isBlank() ? "" : headingBlock.stripTrailing(),
                    headingPath,
                    headingLevels,
                    sectionTitle,
                    startLineNumber,
                    endLineNumber);
            return List.of(chunk);
        }

        if (bodyText.length() <= chunkingOptions.maxChunkSizeCharacters()) {
            String text = headingBlock.isBlank() ? bodyText : headingBlock + bodyText;
            DocumentChunk chunk = new DocumentChunk(startingIndex, text, headingPath, headingLevels, sectionTitle, startLineNumber, endLineNumber);
            return List.of(chunk);
        }

        int step = Math.max(1, chunkingOptions.maxChunkSizeCharacters() - chunkingOptions.overlapCharacters());
        List<DocumentChunk> documentChunks = new ArrayList<>();
        for (int start = 0; start < bodyText.length(); start += step) {
            int end = Math.min(start + chunkingOptions.maxChunkSizeCharacters(), bodyText.length());
            String bodySlice = bodyText.substring(start, end);
            String text = headingBlock.isBlank() ? bodySlice : headingBlock + bodySlice;
            documentChunks.add(new DocumentChunk(startingIndex + documentChunks.size(), text, headingPath, headingLevels, sectionTitle, startLineNumber, endLineNumber));
            if (end >= bodyText.length()) {
                break;
            }
        }
        return documentChunks;
    }

    private String renderHeadingBlock() {
        if (headingPath == null || headingPath.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < headingPath.size(); index++) {
            String title = headingPath.get(index);
            int level = headingLevels.get(index);
            builder.append("#".repeat(level)).append(" ").append(title).append(System.lineSeparator());
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }
}
