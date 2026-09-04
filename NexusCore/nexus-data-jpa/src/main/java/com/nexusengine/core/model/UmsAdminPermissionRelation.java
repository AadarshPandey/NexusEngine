package com.nexusengine.core.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ums_admin_permission_relation")
public class UmsAdminPermissionRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "permission_id")
    private Long permissionId;

    private Integer type;
}
