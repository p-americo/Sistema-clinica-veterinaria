package br.com.clinicavet.clinica_api.model;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)

public class EAgendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataAgendamento;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private EAnimal animal;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ECliente pessoa;

    @Column(length = 20, nullable = false)
    private String status;
}
