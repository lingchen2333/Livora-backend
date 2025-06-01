package com.lingchen.buynow.service.image;

import com.lingchen.buynow.dto.ImageDto;
import com.lingchen.buynow.entity.Image;
import com.lingchen.buynow.entity.Product;
import com.lingchen.buynow.repository.ImageRepository;
import com.lingchen.buynow.repository.ProductRepository;
import com.lingchen.buynow.service.product.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found!"));
    }

    @Override
    public void deleteImageById(Long imageId) {
        Image imageToDelete = this.getImageById(imageId);
        Product productAffected = productRepository.findByImageId(imageId);
        productAffected.getImages().remove(imageToDelete);
        imageRepository.delete(imageToDelete);
    }

    @Override
    public Image updateImage(MultipartFile file, Long imageId) {
        Image image = this.getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            return imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
        Product product = productService.getProductById(productId);

        List<ImageDto> savedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                Image savedImage = imageRepository.save(image);
                String downloadUrl = "/api/v1/images/" + savedImage.getId();
                savedImage.setDownloadUrl(downloadUrl);
                imageRepository.save(savedImage);

//                ImageDto imageDto = new ImageDto();
//                imageDto.setFileName(savedImage.getFileName());
//                imageDto.setId(savedImage.getId());
//                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                ImageDto imageDto = this.convertToDto(savedImage);

                savedImages.add(imageDto);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImages;
    }

    @Override
    public ImageDto convertToDto(Image image) {
        return modelMapper.map(image, ImageDto.class);
    }

    @Override
    public List<ImageDto> getConvertedImages(List<Image> images) {
        return images.stream()
                .map(this::convertToDto).toList();
    }
}
