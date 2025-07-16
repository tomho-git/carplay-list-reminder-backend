package com.carplayPackage.repository;

import com.carplayPackage.model.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    
    List<FamilyMember> findByFamilyIdAndIsActiveOrderByJoinedAtAsc(Long familyId, Boolean isActive);
    
    List<FamilyMember> findByUserIdAndIsActiveOrderByJoinedAtDesc(Long userId, Boolean isActive);
    
    Optional<FamilyMember> findByFamilyIdAndUserId(Long familyId, Long userId);
    
    @Query("SELECT fm FROM FamilyMember fm WHERE fm.family.id = :familyId AND fm.user.id = :userId AND fm.isActive = true")
    Optional<FamilyMember> findActiveMemberByFamilyAndUser(@Param("familyId") Long familyId, @Param("userId") Long userId);
    
    @Query("SELECT fm FROM FamilyMember fm WHERE fm.family.id = :familyId AND fm.role = 'ADMIN' AND fm.isActive = true")
    List<FamilyMember> findAdminsByFamilyId(@Param("familyId") Long familyId);
    
    Long countByFamilyIdAndIsActive(Long familyId, Boolean isActive);
    
    boolean existsByFamilyIdAndUserIdAndIsActive(Long familyId, Long userId, Boolean isActive);
}