package com.nexusengine.core.domain.promotion.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class FlashSaleSlotRepositoryTest {

    private final FlashSaleSlotRepository repository;

    @Autowired
    FlashSaleSlotRepositoryTest(FlashSaleSlotRepository repository) {
        this.repository = repository;
    }

    @Test
    void testContextLoads() {
        assertNotNull(repository, "Repository should be autowired");
    }
}
