package com.lingchen.livora.service.image;

import com.lingchen.livora.dto.ImageDto;
import com.lingchen.livora.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IImageService {
    Image getImageById(Long imageId);
    void deleteImageById(Long imageId);
    Image updateImage(MultipartFile file, Long imageId);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);

    ImageDto convertToDto(Image image);
    List<ImageDto> getConvertedImages(List<Image> images);
}
