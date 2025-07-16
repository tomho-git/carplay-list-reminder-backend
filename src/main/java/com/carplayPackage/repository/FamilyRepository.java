package com.carplayPackage.repository;

import com.carplayPackage.model.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {
    
    Optional<Family> findByFamilyCode(String familyCode);
    
    List<Family> findByCreatedByIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT f FROM Family f JOIN f.members fm WHERE fm.user.id = :userId AND fm.isActive = true ORDER BY f.createdAt DESC")
    List<Family> findFamiliesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT f FROM Family f WHERE f.name LIKE %:name%")
    List<Family> findByNameContaining(@Param("name") String name);
    
    boolean existsByFamilyCode(String familyCode);
}