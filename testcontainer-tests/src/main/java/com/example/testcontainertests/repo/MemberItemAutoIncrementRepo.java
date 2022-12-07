package com.example.testcontainertests.repo;

import com.example.testcontainertests.entity.MemberItemAutoIncrement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberItemAutoIncrementRepo extends JpaRepository<MemberItemAutoIncrement, Long> {
}