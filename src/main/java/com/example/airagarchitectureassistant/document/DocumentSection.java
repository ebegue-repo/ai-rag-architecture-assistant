package com.example.airagarchitectureassistant.document;

import java.util.ArrayList;
import java.util.List;

public class DocumentSection {

    private final List<String> headingPath;
    private final List<Integer> headingLevels;
    private final String sectionTitle;
    private final List<SourceLine> sourceLines = new ArrayList<>();
    private int startLineNumber;
    private int endLineNumber;

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

    public void appendLine(String value, int lineNumber) {
        sourceLines.add(new SourceLine(lineNumber, value == null ? "" : value));
        endLineNumber = lineNumber;
    }

    public List<DocumentChunk> toChunks(ChunkingOptions chunkingOptions, int startingIndex) {
        String headingBlock = renderHeadingBlock();
        String bodyText = renderBodyText();

        if (sourceLines.isEmpty()) {
            return List.of(new DocumentChunk(
                    startingIndex,
                    headingBlock,
                    headingPath,
                    headingLevels,
                    sectionTitle,
                    startLineNumber,
                    endLineNumber));
        }

        if (headingPath.isEmpty() && bodyText.isBlank()) {
            return List.of();
        }

        int availableBodyCharacters = Math.max(1, chunkingOptions.maxChunkSizeCharacters() - headingBlock.length());
        if (bodyText.length() <= availableBodyCharacters) {
            String text = headingBlock + bodyText;
            return List.of(new DocumentChunk(
                    startingIndex,
                    text,
                    headingPath,
                    headingLevels,
                    sectionTitle,
                    startLineNumber,
                    endLineNumber));
        }

        int step = Math.max(1, availableBodyCharacters - chunkingOptions.overlapCharacters());
        List<DocumentChunk> documentChunks = new ArrayList<>();
        for (int start = 0; start < bodyText.length(); start += step) {
            int end = Math.min(start + availableBodyCharacters, bodyText.length());
            String bodySlice = bodyText.substring(start, end);
            String text = headingBlock + bodySlice;
            documentChunks.add(new DocumentChunk(
                    startingIndex + documentChunks.size(),
                    text,
                    headingPath,
                    headingLevels,
                    sectionTitle,
                    startLineNumber,
                    endLineNumber));
            if (end >= bodyText.length()) {
                break;
            }
        }
        return documentChunks;
    }

    private String renderHeadingBlock() {
        if (headingPath.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < headingPath.size(); index++) {
            String title = headingPath.get(index);
            int level = headingLevels.get(index);
            builder.append("#".repeat(level)).append(' ').append(title).append('\n');
        }
        builder.append('\n');
        return builder.toString();
    }

    private String renderBodyText() {
        if (sourceLines.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < sourceLines.size(); index++) {
            if (index > 0) {
                builder.append('\n');
            }
            builder.append(sourceLines.get(index).text());
        }
        return builder.toString();
    }

    private record SourceLine(int lineNumber, String text) {
    }
}
