package com.increff.pos.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.increff.pos.pojo.PojoConstants.USER_SEQUENCE;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(allocationSize = 1,
            name = USER_SEQUENCE,
            sequenceName = USER_SEQUENCE)
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = USER_SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private boolean verified;
}
