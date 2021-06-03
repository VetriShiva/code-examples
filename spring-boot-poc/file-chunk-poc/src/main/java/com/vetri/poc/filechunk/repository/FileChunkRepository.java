package com.vetri.poc.filechunk.repository;

import com.vetri.poc.filechunk.model.FileChunk;

public interface FileChunkRepository extends IGenericDao<FileChunk> {
    FileChunk findLastChunk(String hash);
}
