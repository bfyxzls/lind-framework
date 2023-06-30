package com.lind.common.core.raft;

/**
 * @author lind
 * @date 2023/6/25 11:14
 * @since 1.0.0
 */
// 节点状态
enum NodeState {
    FOLLOWER,
    CANDIDATE,
    LEADER
}
