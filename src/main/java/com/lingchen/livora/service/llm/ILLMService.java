package com.lingchen.livora.service.llm;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ILLMService {
    String describeImage(MultipartFile image) throws IOException;
}
