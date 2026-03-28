package entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity{
    @Column(name = "name", unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone", unique = true)
    private String phone;



    ///@OneToMany(fetch = FetchType.LAZY)
    //@Column(name= "advertisement")
    //private List<Advertisement> advertisements;


//    @ManyToMany(fetch = FetchType.EAGER) //подгружаются все роли сразу
//    @JoinTable(name="user_roles",
//            joinColumns = {@JoinColumn(name="user_id")},
//            inverseJoinColumns = {@JoinColumn(name="role_id")})
//    private List<Role> roles;
}
