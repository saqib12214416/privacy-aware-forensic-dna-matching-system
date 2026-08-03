package com.forensicdna.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "str_loci")
public class StrLocus {

    @Id
    private Integer id;

    private String locus;

    public Integer getId() { return id; }
    public String getLocus() { return locus; }
}