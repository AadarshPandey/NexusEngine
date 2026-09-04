package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsAlbumPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PmsAlbumPicRepository extends JpaRepository<PmsAlbumPic, Long>, JpaSpecificationExecutor<PmsAlbumPic> {
}
