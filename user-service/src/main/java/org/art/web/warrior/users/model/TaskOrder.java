package org.art.web.warrior.users.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task_orders")
public class TaskOrder {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "task_name_id")
    @NotBlank
    private String nameId;

    @Column(name = "task_name")
    @NotBlank
    private String name;

    @Column(name = "task_description")
    private String description;

    @Column(name = "reg_date")
    @EqualsAndHashCode.Exclude
    private LocalDateTime regDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
}
