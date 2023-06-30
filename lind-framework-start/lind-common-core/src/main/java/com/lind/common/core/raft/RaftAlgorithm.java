package com.lind.common.core.raft;

import java.util.ArrayList;

/**
 * @author lind
 * @date 2023/6/25 11:15
 * @since 1.0.0
 */
public class RaftAlgorithm {

	public static void main(String[] args) {
		// 创建 Raft 节点
		RaftNode node = new RaftNode();
		node.peers = new ArrayList<NodeInfo>() {
			{
				add(new NodeInfo(1));
				add(new NodeInfo(2));
				add(new NodeInfo(3));
			}
		};
		// 配置节点信息、对等节点等

		// 启动 Raft 节点
		node.startElection();
	}

}
