package com.lingchen.livora.service.chroma;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import static org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingResponse;

public interface IChromaService {
    void deleteCollection(String collectionName);

    List<Collection> getCollections();

    GetEmbeddingResponse getEmbedding(String collectionId);


    String saveEmbeddingForImage(MultipartFile image, Long imageId, Long productId) throws IOException;

    Set<Long> searchImageSimilarity(MultipartFile image) throws IOException;

    String deleteEmbeddingForImage(Long imageId);
}
