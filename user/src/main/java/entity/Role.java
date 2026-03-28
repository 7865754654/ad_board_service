//package entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//import java.util.List;
//
//@Data
//@Entity
//@EqualsAndHashCode(callSuper = true)
//@Table(name = "roles")
//public class Role extends BaseEntity{
//    @Column(name = "name")
//    private  String name;
//
//    @ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
//    @CollectionTable(name = "role_permission", joinColumns = @JoinColumn(name = "role_id"))
//    @Column(name = "permission")
//    @Enumerated(EnumType.STRING)
//    private List<Permission> permissions;
//
//    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
//    private List<User> users;
//}
