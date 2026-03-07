package com.br.vidya.model;

import com.br.vidya.model.converter.IcmsTypeConverter;
import com.br.vidya.model.converter.PersonTypeConverter;
import com.br.vidya.model.enums.IcmsType;
import com.br.vidya.model.enums.PersonType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @Column(name = "cod_parc")
    private Long codParc;

    @Column(name = "nome_parc", nullable = false)
    private String nomeParc;

    @Column(name = "razao_social")
    private String razaoSocial;

    @Column(name = "cgc_cpf", length = 20)
    private String cgcCpf;

    @Column(name = "person_type", nullable = false)
    @Convert(converter = PersonTypeConverter.class)
    private PersonType personType;

    @Column(name = "classif_icms", length = 5)
    @Convert(converter = IcmsTypeConverter.class)
    private IcmsType classificms;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_cid")
    private City city;

}
