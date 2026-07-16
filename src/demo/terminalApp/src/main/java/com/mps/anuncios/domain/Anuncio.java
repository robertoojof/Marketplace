package com.mps.anuncios.domain;

import java.math.BigDecimal;
import java.util.UUID;

import com.mps.produtos.domain.Produto;
import com.mps.users.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "anuncios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Anuncio {

    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull(message = "Anúncio precisa de um produto")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotNull(message = "Anúncio precisa de um vendedor")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private User vendedor;

    @NotNull(message = "Preço não pode ser vazio")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    @NotNull(message = "Quantidade em estoque não pode ser vazia")
    @Min(value = 0, message = "Quantidade em estoque não pode ser negativa")
    @Column(name = "quantidade_em_estoque", nullable = false)
    private Integer quantidadeEmEstoque;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;
}
