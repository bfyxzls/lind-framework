package com.lind.common.core.raft;

import java.util.List;

/**
 * @author lind
 * @date 2023/6/25 11:14
 * @since 1.0.0
 */
class RaftNode {

	int candidateId;

	int leaderId;

	int nodeId;

	int currentTerm;

	int votedFor;

	NodeState state;

	// 其他节点信息
	List<NodeInfo> peers;

	// 在 Raft 中的日志条目
	List<LogEntry> log;

	// 定时器
	Timer timer;

	// 接收到来自 leader 的心跳时调用
	void onHeartbeatReceived() {
		resetTimer();
	}

	// 接收到来自 candidate 的投票请求时调用
	void onRequestVoteReceived(int candidateTerm) {
		boolean voteGranted = false;

		if (candidateTerm > currentTerm) {
			// 更新自己的 term
			currentTerm = candidateTerm;
			// 清除已投票记录
			votedFor = -1;
			// 变为 follower
			state = NodeState.FOLLOWER;
		}

		if ((votedFor == -1 || votedFor == candidateId) && candidateTerm == currentTerm) {
			// 如果自己还没投票或已经投给了当前候选人，并且候选人的 term 与自己相同
			// 则授予投票权
			voteGranted = true;
			votedFor = candidateId;
			resetTimer();
		}

		// 发送投票结果给候选人
		sendVoteResponse(candidateId, currentTerm, voteGranted);
	}

	// 接收到来自 leader 的日志条目追加请求时调用
	void onAppendEntriesReceived(int leaderTerm, List<LogEntry> entries) {
		boolean success = false;

		if (leaderTerm >= currentTerm) {
			// 更新自己的 term
			currentTerm = leaderTerm;
			// 变为 follower
			state = NodeState.FOLLOWER;
			// 处理日志条目
			success = processLogEntries(entries);
			resetTimer();
		}

		// 发送追加结果给 leader
		sendAppendEntriesResponse(leaderId, currentTerm, success);
	}

	// 处理日志条目
	boolean processLogEntries(List<LogEntry> entries) {
		// 比较日志条目，进行一致性检查和应用
		// ...
		return true;
	}

	// 确定节点集群中的多数派
	int majority(int totalNodes) {
		return (totalNodes / 2) + 1;
	}

	// 发起选举
	void startElection() {
		state = NodeState.CANDIDATE;
		currentTerm++;
		votedFor = nodeId;
		int votesReceived = 1;

		// 发送选举请求给其他节点
		for (NodeInfo peer : peers) {
			sendRequestVote(peer.id, currentTerm, nodeId);
		}

		resetTimer();

		while (state == NodeState.CANDIDATE && votesReceived <= majority(peers.size())) {
			// 等待投票结果
			// ...
		}

		if (state == NodeState.CANDIDATE && votesReceived > majority(peers.size())) {
			// 成为 leader
			state = NodeState.LEADER;
			// 初始化各种数据结构
			// ...
		}
	}

	// 重置定时器
	void resetTimer() {
		// 重置定时器
		// ...
	}

	// 发送请求投票消息
	void sendRequestVote(int candidateId, int term, int candidateLastLogIndex) {
		// 发送请求投票消息
		// ...
	}

	// 发送投票结果消息
	void sendVoteResponse(int candidateId, int term, boolean voteGranted) {
		// 发送投票结果消息
		// ...
	}

	// 发送追加日志条目消息
	void sendAppendEntries(int leaderId, int term, List<LogEntry> entries) {
		// 发送追加日志条目消息
		// ...
	}

	// 发送追加结果消息
	void sendAppendEntriesResponse(int leaderId, int term, boolean success) {
		// 发送追加结果消息
		// ...
	}

}
