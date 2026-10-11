package com.nexusengine.core.ai.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.embedding.Embedding;
import java.util.List;

@Configuration
public class MockEmbeddingConfig {
    @Bean
    @ConditionalOnMissingBean
    public EmbeddingModel mockEmbeddingModel() {
        return new EmbeddingModel() {
            @Override
            public EmbeddingResponse call(EmbeddingRequest request) {
                return new EmbeddingResponse(List.of(new Embedding(new float[1536], 0)));
            }
            @Override
            public float[] embed(Document document) {
                return new float[1536];
            }
            @Override
            public int dimensions() {
                return 1536;
            }
        };
    }
}
