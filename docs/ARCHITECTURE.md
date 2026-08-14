# Architecture

## Overview

The AI RAG Architecture Assistant will allow engineers to query
architecture documentation using natural language.

The system will:

1. Ingest architecture documents.
2. Split documents into meaningful chunks.
3. Generate embeddings for the chunks.
4. Store the embeddings in a vector database.
5. Retrieve relevant chunks for a user question.
6. Provide the retrieved context to an LLM.
7. Return an answer with references to the source documents.

## High-level architecture

```text
Documents
    |
    v
Document Ingestion
    |
    v
Chunking
    |
    v
Embedding Generation
    |
    v
Vector Database
    |
    v
Semantic Retrieval
    |
    v
   LLM
    |
    v
Answer + Sources