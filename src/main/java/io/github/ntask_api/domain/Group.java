//package io.github.ntask_api.domain;
//
//import java.io.Serializable;
//import java.util.Set;
//import javax.persistence.*;
//
//import io.github.ntask_api.service.dto.GroupDTO;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;
//
///**
// * A Group.
// */
//@Entity
//@Table(name = "t_group")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Data
//@EqualsAndHashCode(exclude = { "userGroups" })
//@ToString(exclude = { "userGroups" })
//@NoArgsConstructor
//public class Group extends AbstractAuditingEntity<Long> implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//
//    @OneToMany(mappedBy = "group")
//    private Set<UserGroup> userGroups;
//
//    public Group(Long id) {
//        this.id = id;
//    }
//
//    public Group(String name) {
//        this.name = name;
//    }
//
//    public Group(GroupDTO groupDto) {
//        this(groupDto.getName());
//    }
//}
