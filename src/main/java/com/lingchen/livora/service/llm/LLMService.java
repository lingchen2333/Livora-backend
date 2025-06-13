package com.lingchen.livora.service.llm;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@Service
public class LLMService implements ILLMService {

    private final ChatModel chatModel;

    public String describeImage(MultipartFile image) throws IOException {
        Resource resource = new InputStreamResource(image.getInputStream());
        String contentType = image.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("Image content type must not be null");
        }
        String content = ChatClient.create(chatModel)
                .prompt()
                .user(userPromptSpec -> userPromptSpec
                        .text(
                                """
                                        Generate a concise, detailed textual description of the image strictly for visual similarity search. \
                                        Please follow these rules:
                                        - Limit the description to 2-3 short sentences or a list of key attributes.
                                        - Focus ONLY on clearly visible, distinctive visual features such as:
                                          * Colors, patterns, textures, shapes, and materials.
                                          * Specific object types (e.g., 'red leather handbag', 'wooden dining table').
                                          * Brand names or logos if clearly visible.
                                          * Scene context ONLY if obvious (e.g., 'on a beach', 'in a kitchen').
                                        - Do NOT include subjective opinions, guesses, or generic terms like 'product', 'item',
                                         'electronics device', 'communication device', etc.
                                        - Avoid filler words or vague language.
                                        - Use simple, direct language suitable for automated similarity matching.
                                        Provide the description in a clear, structured format (e.g., comma-separated attributes or bullet points).
                                        """
                        )
                        .media(MediaType.parseMediaType(contentType), resource)
                )
                .call()
                .content();
        log.info("The image description: {}", content);
        return content;
    }
}
