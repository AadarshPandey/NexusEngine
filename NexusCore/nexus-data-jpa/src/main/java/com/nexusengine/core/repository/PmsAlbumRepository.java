package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PmsAlbumRepository extends JpaRepository<PmsAlbum, Long>, JpaSpecificationExecutor<PmsAlbum> {
}
