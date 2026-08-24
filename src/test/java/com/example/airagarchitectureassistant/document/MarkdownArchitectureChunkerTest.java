package com.example.airagarchitectureassistant.document;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

class MarkdownArchitectureChunkerTest {

    @Test
    void chunkerPreservesMultipleSectionsAndDocumentOrder() {
        String markdown = """
                # Overview
                System overview text.

                # Security
                Security details.
                """;

        var chunks = new MarkdownArchitectureChunker(new ChunkingOptions(2000, 100)).chunk(markdown);

        assertThat(chunks).hasSize(2);
        assertThat(chunks).extracting(DocumentChunk::headingPath)
                .containsExactly(List.of("Overview"), List.of("Security"));
    }

    @Test
    void chunkerPreservesSectionAndSubsectionContext() {
        String markdown = """
                # System Design
                Core architecture details.

                ## Application Layer
                Service logic lives here.

                ### API Gateway
                Routing and policy enforcement.
                """;

        var chunks = new MarkdownArchitectureChunker(new ChunkingOptions(2000, 100)).chunk(markdown);

        assertThat(chunks).hasSize(3);
        assertThat(chunks.get(0).text()).contains("# System Design");
        assertThat(chunks.get(1).text()).contains("## Application Layer");
        assertThat(chunks.get(2).text()).contains("### API Gateway");
    }

    @Test
    void sectionThatFitsInSingleChunkStaysUnsplit() {
        String markdown = """
                ## Services
                We keep the service layer independent from the presentation layer.
                """;

        var chunks = new MarkdownArchitectureChunker(new ChunkingOptions(500, 50)).chunk(markdown);

        assertThat(chunks).hasSize(1);
        assertThat(chunks.get(0).text()).contains("## Services");
        assertThat(chunks.get(0).text()).contains("independent");
    }

    @Test
    void largeSectionIsSplitWithOverlap() {
        String body = "ABCD".repeat(80);
        String markdown = "# Large Section\n" + body;

        var chunker = new MarkdownArchitectureChunker(new ChunkingOptions(80, 20));
        var chunks = chunker.chunk(markdown);

        assertThat(chunks).hasSizeGreaterThan(1);
        assertThat(chunks.get(1).text()).contains(chunks.get(0).text().substring(chunks.get(0).text().length() - 20));
    }

    @Test
    void chunkerHandlesDocumentsWithoutHeadings() {
        String markdown = "Plain text paragraph.\nAnother line of context.";

        var chunks = new MarkdownArchitectureChunker(new ChunkingOptions(200, 25)).chunk(markdown);

        assertThat(chunks).hasSize(1);
        assertThat(chunks.get(0).headingPath()).isEmpty();
        assertThat(chunks.get(0).text()).contains("Plain text paragraph");
    }

    @Test
    void emptyInputProducesNoChunks() {
        var chunker = new MarkdownArchitectureChunker(new ChunkingOptions(200, 25));

        assertThat(chunker.chunk("")).isEmpty();
        assertThat(chunker.chunk("   ")).isEmpty();
    }

    @Test
    void consecutiveHeadingsWithLittleOrNoContentRemainDeterministic() {
        String markdown = """
                # First

                ## Second

                ### Third
                """;

        var chunker = new MarkdownArchitectureChunker(new ChunkingOptions(200, 25));
        var chunks = chunker.chunk(markdown);
        var sameChunks = chunker.chunk(markdown);

        assertThat(chunks).extracting(DocumentChunk::headingPath)
                .containsExactly(List.of("First"), List.of("First", "Second"), List.of("First", "Second", "Third"));
        assertThat(chunks).usingRecursiveComparison().isEqualTo(sameChunks);
    }

    @Test
    void invalidChunkConfigurationFailsFast() {
        assertThatThrownBy(() -> new ChunkingOptions(0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Maximum chunk size");

        assertThatThrownBy(() -> new ChunkingOptions(50, 50))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Overlap");
    }
}
