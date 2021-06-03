package com.vetri.poc.filechunk.repository;

import com.vetri.poc.filechunk.model.FileChunk;
import com.vetri.poc.filechunk.utils.DaoUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;

@Transactional
@Repository
public class FileChunkRepositoryImpl extends AbstractGenericDao<FileChunk> implements FileChunkRepository {

    @Override
    public FileChunk findLastChunk(final String hash){
        String hql = "Select f from FileChunk f LEFT JOIN FETCH f.metadata m where m.hash = :pHash order by f.chunkNo" +
                " desc";
        Query query = em.createQuery(hql);
        query.setParameter("pHash", hash);
        return (FileChunk) DaoUtil.firstOrNull(query.getResultList());
    }
}
