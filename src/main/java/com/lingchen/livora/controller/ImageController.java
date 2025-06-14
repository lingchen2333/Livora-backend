package com.lingchen.livora.controller;


import com.lingchen.livora.dto.ImageDto;
import com.lingchen.livora.entity.Image;
import com.lingchen.livora.response.ApiResponse;
import com.lingchen.livora.service.image.IImageService;
import com.lingchen.livora.service.llm.LLMService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}")
public class ImageController {

    private final IImageService imageService;
    private final LLMService llmService;


    @PostMapping("/products/{productId}/images")
    public ResponseEntity<ApiResponse> uploadImages(
            @RequestParam("files") List<MultipartFile> files,
            @PathVariable("productId") Long productId) {
        List<ImageDto> imageDto = imageService.saveImages(productId, files);
        return ResponseEntity.ok(new ApiResponse("Images uploaded successfully!", imageDto));
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ApiResponse> deleteImageById(@PathVariable Long imageId) throws IOException {
        imageService.deleteImageById(imageId);
        return ResponseEntity.ok(new ApiResponse("Image deleted successfully", null));
    }

    @PutMapping("/images/{imageId}")
    public ResponseEntity<ApiResponse> updateImageById(@PathVariable Long imageId, @RequestParam("file") MultipartFile file) {
        Image image = imageService.updateImage(file, imageId);
        return ResponseEntity.ok(new ApiResponse("Image updated successfully", imageService.convertToDto(image)));
    }

    @GetMapping("/images/describe")
    public ResponseEntity<ApiResponse> describeImage(@RequestParam("image") MultipartFile image) throws IOException {
        String description = llmService.describeImage(image);
        return ResponseEntity.ok(new ApiResponse("Image describe successfully", description));
    }
}

