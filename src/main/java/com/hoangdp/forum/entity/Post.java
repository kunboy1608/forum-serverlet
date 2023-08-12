package com.hoangdp.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Table(name = "posts")
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String title;

    private String content;

    @Transient
    private Long comment;

    @Transient
    private Long heart;    
}
