package com.waracle.cakemgr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "CAKE_ENTITY")
public class CakeEntity implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer cakeId;

    @Column(name = "TITLE", length = 100)
    private String title;

    @Column(name = "DESCRIPTION",   length = 100)
    private String description;

    @Column(name = "IMAGE", length = 300)
    private String image;
}