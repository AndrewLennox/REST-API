package com.example.AssignmentOne;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "styles")
public class Styles implements Serializable {

    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)  
    private Integer style_Id;
    private String p_Style;
    
}
