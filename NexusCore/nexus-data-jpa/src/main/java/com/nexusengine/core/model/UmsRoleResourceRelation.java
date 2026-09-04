package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ums_role_resource_relation")
public class UmsRoleResourceRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id")
    @Schema(title = "Role id")
    private Long roleId;

    @Column(name = "resource_id")
    @Schema(title = "Resource id")
    private Long resourceId;
}
