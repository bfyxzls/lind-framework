package com.lind.activiti.repository;

import com.lind.activiti.entity.ActReNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

// 自定义接口 不会创建接口的实例 必须加此注解
@Repository
public interface ActReNodeRepository extends JpaRepository<ActReNode, String> {

	ActReNode findByNodeIdAndProcessDefId(String nodeId, String processDefId);

}
