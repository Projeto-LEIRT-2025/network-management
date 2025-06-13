package com.github.project.networkservice.models

class Node(

    val id: String,
    val label: String

)

class Edge(

    val source: Node,
    val target: Node,
    val sourceInterface: String,
    val targetInterface: String

) {

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (other !is Edge) return false

        val direct = source.id == other.source.id &&
                target.id == other.target.id &&
                sourceInterface == other.sourceInterface &&
                targetInterface == other.targetInterface

        val reversed = source.id == other.target.id &&
                target.id == other.source.id &&
                sourceInterface == other.targetInterface &&
                targetInterface == other.sourceInterface

        return direct || reversed
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + target.hashCode()
        result = 31 * result + sourceInterface.hashCode()
        result = 31 * result + targetInterface.hashCode()
        return result
    }

}

class Graph {

    private val _nodes = mutableMapOf<String, Node>()
    private val _edges = mutableSetOf<Edge>()

    val nodes: List<Node>
        get() = _nodes.values.toList()

    val edges: Set<Edge>
        get() = _edges

    fun addNode(node: Node) {
        _nodes.putIfAbsent(node.id, node)
    }

    fun addEdge(sourceId: String, targetId: String, sourceInterface: String, targetInterface: String) {

        if (sourceId == targetId)
            return

        val source = _nodes[sourceId] ?: throw IllegalArgumentException("Node $sourceId doesn't exist")
        val target = _nodes[targetId] ?: throw IllegalArgumentException("Node $targetId doesn't exist")
        val edge = Edge(source, target, sourceInterface, targetInterface)

        _edges.add(edge)
    }

}