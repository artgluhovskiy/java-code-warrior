package org.art.web.warrior.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @Column(name = "task_name_id")
    private String nameId;

    @NotNull
    @Column(name = "task_name")
    private String name;

    @NotNull
    @Column(name = "task_description")
    private String description;

    @EqualsAndHashCode.Exclude
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onRegistration() {
        this.regDate = LocalDateTime.now();
    }
}
