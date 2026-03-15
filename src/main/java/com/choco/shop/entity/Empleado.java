package com.choco.shop.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "empleados")
public class Empleado extends User {

    private BigDecimal sueldo;
}
