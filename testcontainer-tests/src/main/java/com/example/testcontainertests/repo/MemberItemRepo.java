package com.example.testcontainertests.repo;

import com.example.testcontainertests.entity.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberItemRepo extends JpaRepository<MemberItem, Long> {
}