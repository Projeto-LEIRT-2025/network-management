package com.github.project.networkservice.models

class Node(

    val id: String,
    val label: String

)

class Edge(

    val source: Node,
    val target: Node

)

class Graph {

    private val _nodes = mutableMapOf<String, Node>()
    private val _edges = mutableListOf<Edge>()

    val nodes: List<Node>
        get() = _nodes.values.toList()

    val edges: List<Edge>
        get() = _edges

    fun addNode(node: Node) {
        _nodes.putIfAbsent(node.id, node)
    }

    fun addEdge(sourceId: String, targetId: String) {

        if (sourceId == targetId)
            return

        val source = _nodes[sourceId] ?: throw IllegalArgumentException("Node $sourceId doesn't exist")
        val target = _nodes[targetId] ?: throw IllegalArgumentException("Node $targetId doesn't exist")
        val edge = Edge(source, target)

        _edges.add(edge)
    }

}