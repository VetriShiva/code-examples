package com.vetri.poc.filechunk.repository;

import com.vetri.poc.filechunk.model.FileMetadata;

public interface FileMetadataRepository extends com.vetri.poc.filechunk.repository.IGenericDao<FileMetadata> {
    FileMetadata findByHash(String hash);

    FileMetadata findByUniqueId(String uniqueId);
}
