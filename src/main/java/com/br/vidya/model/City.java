package com.br.vidya.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {

    @Id
    @Column(name = "cod_cid")
    private Long codCid;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "uf", length = 2)
    private String uf;
}
