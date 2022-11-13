//package io.github.ntask_api.domain;
//
//import java.io.Serializable;
//import javax.persistence.*;
//
//import io.github.ntask_api.service.dto.GroupEventDTO;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;
//
///**
// * A GroupEvent.
// */
//@Entity
//@Table(name = "group_event")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Data
//@EqualsAndHashCode(exclude = { "group", "event" })
//@ToString(exclude = { "group", "event" })
//@NoArgsConstructor
//public class GroupEvent implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    private Group group;
//
//    @ManyToOne
//    private Event event;
//
//    public GroupEvent(GroupEventDTO groupEventDto) {
//        group = new Group(groupEventDto.getGroup());
//        event = new Event(groupEventDto.getEvent());
//    }
//}


///ad cai log sql do chua, chua


///Unauthorized: Full authentication is required to access this resource dcm login lay token coi bac
