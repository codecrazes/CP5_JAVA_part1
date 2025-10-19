package br.com.fiap.supermarket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "TB_CLIENTE")
public class Cliente {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_seq")
	    @SequenceGenerator(name = "cliente_seq", sequenceName = "SEQ_CLIENTE", allocationSize = 1)
	    private Long id;

	    @Column(nullable = false)
	    private String nome;

	    @Column(name = "cpf", nullable = false, length = 512)
	    private String cpf;
	    
	    @Column(nullable = false)
	    private String telefone;

	    @Column(name = "endereco", nullable = false, length = 1024)
	    private String endereco;
}