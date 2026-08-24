package com.example.airagarchitectureassistant.document;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownArchitectureChunker {

    private static final Pattern HEADING_PATTERN = Pattern.compile("^(#{1,6})\\s+(.*?)(?:\\s+#+\\s*)?$");

    private final ChunkingOptions options;

    public MarkdownArchitectureChunker(ChunkingOptions options) {
        this.options = Objects.requireNonNull(options, "Chunking options are required");
    }

    public List<DocumentChunk> chunk(String markdownDocument) {
        if (markdownDocument == null || markdownDocument.isBlank()) {
            return List.of();
        }

        List<DocumentSection> sections = parseSections(markdownDocument);
        List<DocumentChunk> chunks = new ArrayList<>();
        int chunkIndex = 0;

        for (DocumentSection section : sections) {
            List<DocumentChunk> sectionChunks = section.toChunks(options, chunkIndex);
            chunks.addAll(sectionChunks);
            chunkIndex += sectionChunks.size();
        }

        return List.copyOf(chunks);
    }

    private List<DocumentSection> parseSections(String markdownDocument) {
        String[] lines = markdownDocument.split("\\R", -1);
        List<DocumentSection> sections = new ArrayList<>();
        Deque<String> headingPath = new ArrayDeque<>();
        Deque<Integer> headingLevels = new ArrayDeque<>();
        DocumentSection currentSection = null;

        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            String line = lines[lineIndex];
            Matcher matcher = HEADING_PATTERN.matcher(line);

            if (matcher.matches()) {
                if (currentSection != null) {
                    sections.add(currentSection);
                }

                int level = matcher.group(1).length();
                String title = matcher.group(2).trim();
                while (headingPath.size() >= level) {
                    headingPath.removeLast();
                    headingLevels.removeLast();
                }
                headingPath.addLast(title);
                headingLevels.addLast(level);

                currentSection = new DocumentSection(List.copyOf(headingPath), List.copyOf(headingLevels), title, lineIndex + 1, lineIndex + 1);
                continue;
            }

            if (currentSection == null) {
                if (line.isBlank()) {
                    continue;
                }
                if (sections.isEmpty() || !sections.getLast().getHeadingPath().isEmpty()) {
                    sections.add(new DocumentSection(List.of(), List.of(), "", lineIndex + 1, lineIndex + 1));
                }
                sections.getLast().appendContent(line);
            } else {
                currentSection.appendContent(line);
            }
        }

        if (currentSection != null) {
            sections.add(currentSection);
        }

        return sections;
    }
}
