//package io.github.ntask_api.service.dto;
//
//import io.github.ntask_api.domain.GroupEvent;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//
///**
// * A GroupEvent.
// */
//@Data
//@NoArgsConstructor
//public class GroupEventDTO implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    private Long id;
//
//    private Long group;
//
//    private Long event;
//
//    public GroupEventDTO(GroupEvent ge) {
//        id = ge.getId();
//        group = ge.getGroup().getId();
//        event = ge.getEvent().getId();
//    }
//
//}
