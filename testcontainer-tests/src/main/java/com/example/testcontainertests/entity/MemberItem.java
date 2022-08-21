package com.example.testcontainertests.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Id;

@Getter
@Setter

@Entity
@Table(name = "member_items_no_increment")
public class MemberItem {

    @Id
    private long id;

    @Column(name = "member_id")
    private String memberId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
}
