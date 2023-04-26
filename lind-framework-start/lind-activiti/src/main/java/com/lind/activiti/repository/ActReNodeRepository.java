package com.lind.activiti.repository;

import com.lind.activiti.entity.ActReNode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActReNodeRepository extends JpaRepository<ActReNode, String> {

	ActReNode findByNodeIdAndProcessDefId(String nodeId, String processDefId);

}
