package org.art.web.warrior.users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private LocalDateTime regDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
