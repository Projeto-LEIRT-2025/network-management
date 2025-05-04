package com.github.project.networkservice.models

class Node(

    val id: String,
    val label: String

)

class Edge(

    val from: Node,
    val to: Node

)

class Graph {

    private val _nodes = mutableMapOf<String, Node>()
    private val _edges = mutableListOf<Edge>()

    val edges: List<Edge>
        get() = _edges

    fun addNode(node: Node) {
        _nodes.putIfAbsent(node.id, node)
    }

    fun addEdge(fromId: String, toId: String) {

        val from = _nodes[fromId] ?: throw IllegalArgumentException("Node $fromId doesn't exist")
        val to = _nodes[toId] ?: throw IllegalArgumentException("Node $toId doesn't exist")
        val edge = Edge(from, to)

        _edges.add(edge)
    }

}