package com.lingchen.livora.controller;

import com.lingchen.livora.response.ApiResponse;
import com.lingchen.livora.service.chroma.IChromaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import static org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/collections")
public class ChromaController {

    private final IChromaService chromaService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCollection() {
        List<Collection> collections = chromaService.getCollections();
        return ResponseEntity.ok(new ApiResponse("Collections found!", collections));
    }

    @DeleteMapping("/{collectionName}")
    public ResponseEntity<ApiResponse> deleteCollection(@PathVariable String collectionName) {
        chromaService.deleteCollection(collectionName);
        return ResponseEntity.ok(new ApiResponse("Collection deleted!", collectionName));
    }

    @GetMapping("/{collectionId}/embeddings")
    public ResponseEntity<ApiResponse> getEmbeddingsByCollectionId(@PathVariable String collectionId) {
        GetEmbeddingResponse embedding = chromaService.getEmbedding(collectionId);
        return ResponseEntity.ok(new ApiResponse("Embeddings found!", embedding));
    }


}
