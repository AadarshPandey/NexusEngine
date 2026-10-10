package com.nexusengine.core.domain.engagement.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class PmsReviewAttachmentRepositoryTest {

    private final ReviewMediaRepository repository;

    @Autowired
    PmsReviewAttachmentRepositoryTest(ReviewMediaRepository repository) {
        this.repository = repository;
    }

    @Test
    void testContextLoads() {
        assertNotNull(repository, "Repository should be autowired");
    }
}
