package com.example.airagarchitectureassistant.document;

public record ChunkingOptions(int maxChunkSizeCharacters, int overlapCharacters) {

    public ChunkingOptions {
        if (maxChunkSizeCharacters <= 0) {
            throw new IllegalArgumentException("Maximum chunk size must be greater than zero characters.");
        }
        if (overlapCharacters < 0) {
            throw new IllegalArgumentException("Overlap must be zero or greater.");
        }
        if (overlapCharacters >= maxChunkSizeCharacters) {
            throw new IllegalArgumentException("Overlap must be smaller than the configured maximum chunk size.");
        }
    }

    public static ChunkingOptions defaults() {
        return new ChunkingOptions(1200, 150);
    }
}
