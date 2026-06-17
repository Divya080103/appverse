package com.appverse.repository;

import com.appverse.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    List<AppVersion> findByAppIdOrderByReleasedAtDesc(Long appId);
}
