package com.carplayPackage.controller;

import com.carplayPackage.model.Family;
import com.carplayPackage.model.FamilyMember;
import com.carplayPackage.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/families")
@CrossOrigin(origins = "*")
public class FamilyController {
    
    @Autowired
    private FamilyService familyService;
    
    @GetMapping
    public ResponseEntity<List<Family>> getAllFamilies() {
        List<Family> families = familyService.getAllFamilies();
        return ResponseEntity.ok(families);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Family> getFamilyById(@PathVariable Long id) {
        Optional<Family> family = familyService.getFamilyById(id);
        return family.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{familyCode}")
    public ResponseEntity<Family> getFamilyByCode(@PathVariable String familyCode) {
        Optional<Family> family = familyService.getFamilyByCode(familyCode);
        return family.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Family>> getFamiliesByUserId(@PathVariable Long userId) {
        List<Family> families = familyService.getFamiliesByUserId(userId);
        return ResponseEntity.ok(families);
    }
    
    @GetMapping("/created-by/{userId}")
    public ResponseEntity<List<Family>> getFamiliesCreatedByUser(@PathVariable Long userId) {
        List<Family> families = familyService.getFamiliesCreatedByUser(userId);
        return ResponseEntity.ok(families);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Family>> searchFamiliesByName(@RequestParam String name) {
        List<Family> families = familyService.searchFamiliesByName(name);
        return ResponseEntity.ok(families);
    }
    
    @PostMapping
    public ResponseEntity<Family> createFamily(@RequestParam String name, @RequestParam Long creatorId) {
        Optional<Family> createdFamily = familyService.createFamily(name, creatorId);
        return createdFamily.map(family -> ResponseEntity.status(HttpStatus.CREATED).body(family))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PostMapping("/with-description")
    public ResponseEntity<Family> createFamilyWithDescription(@RequestParam String name, 
                                                            @RequestParam String description,
                                                            @RequestParam Long creatorId) {
        Optional<Family> createdFamily = familyService.createFamily(name, description, creatorId);
        return createdFamily.map(family -> ResponseEntity.status(HttpStatus.CREATED).body(family))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Family> updateFamily(@PathVariable Long id, @RequestBody Family family) {
        Optional<Family> updatedFamily = familyService.updateFamily(id, family);
        return updatedFamily.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable Long id) {
        boolean deleted = familyService.deleteFamily(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{familyCode}/join")
    public ResponseEntity<FamilyMember> joinFamilyByCode(@PathVariable String familyCode, @RequestParam Long userId) {
        Optional<FamilyMember> member = familyService.joinFamilyByCode(familyCode, userId);
        return member.map(familyMember -> ResponseEntity.status(HttpStatus.CREATED).body(familyMember))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PostMapping("/{familyCode}/invite")
    public ResponseEntity<FamilyMember> inviteMemberToFamily(@PathVariable String familyCode, 
                                                           @RequestParam Long userId,
                                                           @RequestParam Long invitedById) {
        Optional<FamilyMember> member = familyService.addMemberToFamily(familyCode, userId, invitedById);
        return member.map(familyMember -> ResponseEntity.status(HttpStatus.CREATED).body(familyMember))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @GetMapping("/{familyId}/members")
    public ResponseEntity<List<FamilyMember>> getFamilyMembers(@PathVariable Long familyId) {
        List<FamilyMember> members = familyService.getFamilyMembers(familyId);
        return ResponseEntity.ok(members);
    }
    
    @GetMapping("/{familyId}/members/{userId}")
    public ResponseEntity<FamilyMember> getFamilyMember(@PathVariable Long familyId, @PathVariable Long userId) {
        Optional<FamilyMember> member = familyService.getFamilyMember(familyId, userId);
        return member.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{familyId}/members/{userId}/is-member")
    public ResponseEntity<Object> isUserMemberOfFamily(@PathVariable Long familyId, @PathVariable Long userId) {
        boolean isMember = familyService.isUserMemberOfFamily(familyId, userId);
        return ResponseEntity.ok(new Object() {
            public final boolean isMember = familyService.isUserMemberOfFamily(familyId, userId);
        });
    }
    
    @GetMapping("/{familyId}/members/{userId}/is-admin")
    public ResponseEntity<Object> isUserAdminOfFamily(@PathVariable Long familyId, @PathVariable Long userId) {
        boolean isAdmin = familyService.isUserAdminOfFamily(familyId, userId);
        return ResponseEntity.ok(new Object() {
            public final boolean isAdmin = familyService.isUserAdminOfFamily(familyId, userId);
        });
    }
    
    @DeleteMapping("/{familyId}/members/{userId}")
    public ResponseEntity<Void> removeMemberFromFamily(@PathVariable Long familyId, @PathVariable Long userId) {
        boolean removed = familyService.removeMemberFromFamily(familyId, userId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{familyId}/members/{userId}/promote")
    public ResponseEntity<FamilyMember> promoteToAdmin(@PathVariable Long familyId, @PathVariable Long userId) {
        Optional<FamilyMember> promoted = familyService.promoteToAdmin(familyId, userId);
        return promoted.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{familyId}/stats")
    public ResponseEntity<Object> getFamilyStats(@PathVariable Long familyId) {
        Long memberCount = familyService.getFamilyMemberCount(familyId);
        return ResponseEntity.ok(new Object() {
            public final Long memberCount = familyService.getFamilyMemberCount(familyId);
        });
    }
}