package com.carplayPackage.service;

import com.carplayPackage.model.Family;
import com.carplayPackage.model.FamilyMember;
import com.carplayPackage.model.User;
import com.carplayPackage.repository.FamilyRepository;
import com.carplayPackage.repository.FamilyMemberRepository;
import com.carplayPackage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FamilyService {
    
    @Autowired
    private FamilyRepository familyRepository;
    
    @Autowired
    private FamilyMemberRepository familyMemberRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Family> getAllFamilies() {
        return familyRepository.findAll();
    }
    
    public Optional<Family> getFamilyById(Long id) {
        return familyRepository.findById(id);
    }
    
    public Optional<Family> getFamilyByCode(String familyCode) {
        return familyRepository.findByFamilyCode(familyCode);
    }
    
    public List<Family> getFamiliesByUserId(Long userId) {
        return familyRepository.findFamiliesByUserId(userId);
    }
    
    public List<Family> getFamiliesCreatedByUser(Long userId) {
        return familyRepository.findByCreatedByIdOrderByCreatedAtDesc(userId);
    }
    
    public List<Family> searchFamiliesByName(String name) {
        return familyRepository.findByNameContaining(name);
    }
    
    @Transactional
    public Optional<Family> createFamily(String name, Long creatorId) {
        Optional<User> creator = userRepository.findById(creatorId);
        if (creator.isPresent()) {
            Family family = new Family(name, creator.get());
            Family savedFamily = familyRepository.save(family);
            
            FamilyMember adminMember = new FamilyMember(savedFamily, creator.get(), FamilyMember.FamilyRole.ADMIN);
            familyMemberRepository.save(adminMember);
            
            return Optional.of(savedFamily);
        }
        return Optional.empty();
    }
    
    @Transactional
    public Optional<Family> createFamily(String name, String description, Long creatorId) {
        Optional<User> creator = userRepository.findById(creatorId);
        if (creator.isPresent()) {
            Family family = new Family(name, creator.get());
            family.setDescription(description);
            Family savedFamily = familyRepository.save(family);
            
            FamilyMember adminMember = new FamilyMember(savedFamily, creator.get(), FamilyMember.FamilyRole.ADMIN);
            familyMemberRepository.save(adminMember);
            
            return Optional.of(savedFamily);
        }
        return Optional.empty();
    }
    
    public Optional<Family> updateFamily(Long id, Family updatedFamily) {
        Optional<Family> existingFamily = familyRepository.findById(id);
        if (existingFamily.isPresent()) {
            Family family = existingFamily.get();
            family.setName(updatedFamily.getName());
            if (updatedFamily.getDescription() != null) {
                family.setDescription(updatedFamily.getDescription());
            }
            return Optional.of(familyRepository.save(family));
        }
        return Optional.empty();
    }
    
    public boolean deleteFamily(Long id) {
        if (familyRepository.existsById(id)) {
            familyRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Transactional
    public Optional<FamilyMember> addMemberToFamily(String familyCode, Long userId, Long invitedById) {
        Optional<Family> family = familyRepository.findByFamilyCode(familyCode);
        Optional<User> user = userRepository.findById(userId);
        Optional<User> invitedBy = userRepository.findById(invitedById);
        
        if (family.isPresent() && user.isPresent() && invitedBy.isPresent()) {
            if (!familyMemberRepository.existsByFamilyIdAndUserIdAndIsActive(family.get().getId(), userId, true)) {
                FamilyMember member = new FamilyMember(family.get(), user.get(), FamilyMember.FamilyRole.MEMBER, invitedBy.get());
                return Optional.of(familyMemberRepository.save(member));
            }
        }
        return Optional.empty();
    }
    
    @Transactional
    public Optional<FamilyMember> joinFamilyByCode(String familyCode, Long userId) {
        Optional<Family> family = familyRepository.findByFamilyCode(familyCode);
        Optional<User> user = userRepository.findById(userId);
        
        if (family.isPresent() && user.isPresent()) {
            if (!familyMemberRepository.existsByFamilyIdAndUserIdAndIsActive(family.get().getId(), userId, true)) {
                FamilyMember member = new FamilyMember(family.get(), user.get(), FamilyMember.FamilyRole.MEMBER);
                return Optional.of(familyMemberRepository.save(member));
            }
        }
        return Optional.empty();
    }
    
    public List<FamilyMember> getFamilyMembers(Long familyId) {
        return familyMemberRepository.findByFamilyIdAndIsActiveOrderByJoinedAtAsc(familyId, true);
    }
    
    public List<FamilyMember> getUserFamilyMemberships(Long userId) {
        return familyMemberRepository.findByUserIdAndIsActiveOrderByJoinedAtDesc(userId, true);
    }
    
    public Optional<FamilyMember> getFamilyMember(Long familyId, Long userId) {
        return familyMemberRepository.findActiveMemberByFamilyAndUser(familyId, userId);
    }
    
    public boolean isUserMemberOfFamily(Long familyId, Long userId) {
        return familyMemberRepository.existsByFamilyIdAndUserIdAndIsActive(familyId, userId, true);
    }
    
    public boolean isUserAdminOfFamily(Long familyId, Long userId) {
        Optional<FamilyMember> member = familyMemberRepository.findActiveMemberByFamilyAndUser(familyId, userId);
        return member.isPresent() && member.get().getRole() == FamilyMember.FamilyRole.ADMIN;
    }
    
    @Transactional
    public boolean removeMemberFromFamily(Long familyId, Long userId) {
        Optional<FamilyMember> member = familyMemberRepository.findActiveMemberByFamilyAndUser(familyId, userId);
        if (member.isPresent()) {
            FamilyMember familyMember = member.get();
            familyMember.setIsActive(false);
            familyMemberRepository.save(familyMember);
            return true;
        }
        return false;
    }
    
    @Transactional
    public Optional<FamilyMember> promoteToAdmin(Long familyId, Long userId) {
        Optional<FamilyMember> member = familyMemberRepository.findActiveMemberByFamilyAndUser(familyId, userId);
        if (member.isPresent()) {
            FamilyMember familyMember = member.get();
            familyMember.setRole(FamilyMember.FamilyRole.ADMIN);
            return Optional.of(familyMemberRepository.save(familyMember));
        }
        return Optional.empty();
    }
    
    public Long getFamilyMemberCount(Long familyId) {
        return familyMemberRepository.countByFamilyIdAndIsActive(familyId, true);
    }
}