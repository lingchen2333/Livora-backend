package com.lingchen.livora.service.chroma;

import com.lingchen.livora.service.llm.ILLMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.ai.chroma.vectorstore.ChromaApi.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChromaService implements IChromaService {

    private final ChromaApi chromaApi;
    private final ILLMService llmService;
    private final ChromaVectorStore chromaVectorStore;

    @Override
    public void deleteCollection(String collectionName) {
        try {
            chromaApi.deleteCollection(collectionName);
        } catch (Exception e) {
            throw new RuntimeException("failed to delete collection: " + collectionName, e);
        }
    }

    @Override
    public List<Collection> getCollections() {
        try {
            return chromaApi.listCollections();
        } catch (Exception e) {
            throw new RuntimeException("failed to get  collections", e);
        }
    }

    @Override
    public GetEmbeddingResponse getEmbedding(String collectionId) {
        try {
            GetEmbeddingsRequest request = new GetEmbeddingsRequest(
                    null, null, 0, 0, ChromaApi.QueryRequest.Include.all
            );
            return chromaApi.getEmbeddings(collectionId, request);
        } catch (Exception e) {
            throw new RuntimeException("failed to get embedding: " + collectionId, e);
        }
    }

    @Override
    public String saveEmbeddingForImage(MultipartFile image, Long imageId, Long productId) throws IOException {
        log.info("saving embedding");
        String description = llmService.describeImage(image);
        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("productId", productId);
        Document document = Document.builder()
                .id(imageId.toString())
                .text(description)
                .metadata(metadata)
                .build();
        try {
            chromaVectorStore.doAdd(List.of(document));
        } catch (Exception e) {
            throw new RuntimeException("failed to save embedding", e);
        }
        return "Document added to chroma store successfully";
    }

    @Override
    public Set<Long> searchImageSimilarity(MultipartFile image) throws IOException {
        String description = llmService.describeImage(image);
        SearchRequest searchRequest = SearchRequest.builder()
                .query(description)
                .topK(10)
                .similarityThreshold(0.85f)
                .build();
        List<Document> documents = chromaVectorStore.doSimilaritySearch(searchRequest);
        log.info("documents result: {}", documents);
        return documents.stream()
                .map(document -> document.getMetadata().get("productId"))
                .map(Object::toString)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    @Override
    public String deleteEmbeddingForImage(Long imageId)  {
        chromaVectorStore.doDelete(List.of(imageId.toString()));
        return "Image deleted successfully";
    }
}
