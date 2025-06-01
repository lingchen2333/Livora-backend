package com.lingchen.buynow.service.image;

import com.lingchen.buynow.dto.ImageDto;
import com.lingchen.buynow.dto.ProductDto;
import com.lingchen.buynow.entity.Image;
import com.lingchen.buynow.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long imageId);
    void deleteImageById(Long imageId);
    Image updateImage(MultipartFile file, Long imageId);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);

    ImageDto convertToDto(Image image);
    List<ImageDto> getConvertedImages(List<Image> images);
}
