package com.vetri.poc.filechunk.service;

import com.vetri.poc.filechunk.model.FileChunk;
import com.vetri.poc.filechunk.model.FileMetadata;

public interface FileUploadService {
    void saveFile(FileMetadata fileMetadata);

    void updateFile(FileMetadata fileMetadata);

    FileMetadata findByHash(String hash);

    FileMetadata findByUniqueId(String uniqueId);

    void saveChunk(FileChunk fileChunk);

    FileChunk findLastChunk(String hash);
}
