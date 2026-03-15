package com.choco.shop.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "clientes")
public class Cliente extends User {

    private String direccion;

    private String telefono;

    private String recibirPromociones;
}
