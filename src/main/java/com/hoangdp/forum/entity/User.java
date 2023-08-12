package com.hoangdp.forum.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    private String password;

    private String avatar;

    private String nickname;
    
    private String salt;
}
