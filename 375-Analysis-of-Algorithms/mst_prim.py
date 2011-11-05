def primMST(vertices, edges):    
    '''
    Determines the edges that make up a minimum spanning tree of a given set of
    vertices and edges of a graph.

    Adapted from code by 'Mike', posted in the comments section of:
    http://programmingpraxis.com/2010/04/09/minimum-spanning-tree-prims-algorithm/

    Note 1: The graph represented by the inputted vertices and edges must be 
            connected.
    Note 2: Each vertex must be a unique integer 0..n-1, where n is the number 
            of vertices.

    @param vertices - a list of vertices
    @param edges - a list of edges that specify two vertices (endpoints) and the
                   weight of the edge.

    @return - a list of the edges that make up a minimum spanning tree.
    ''' 
    # Initialize all parents to None and distances to positive infinity
    parents = [None] * len(vertices)
    distances = [float('inf')] * len(vertices)

    # Make an adjacency list mapping each vertex to the edges connected to it
    adjacencyList = [[] for v in vertices]
    for (vertex1, vertex2, weight) in edges:
        adjacencyList[vertex1].append((vertex1, vertex2, weight))
        adjacencyList[vertex2].append((vertex2, vertex1, weight))

    # start at an any vertex of the graph and set its distance to 0.0
    arbitraryVertex = vertices[0]
    distances[arbitraryVertex] = 0.0

    # keep track of the vertices marked so far
    markedVertices = []
        
    # make min-heap of vertices ordered by their distances
    vertexHeap = IndexMinHeap(len(vertices))
    for vertex in vertices:
        vertexHeap.insert(vertex, distances[vertex])
            
    while len(markedVertices) < len(vertices):
        
        # pop minimum distance vertex off heap and mark it
        distance = vertexHeap.minKey()
        vertex = vertexHeap.delMin()
        markedVertices.append(vertex)

        # scan the distances of adjacent vertices
        adjacentEdges = adjacencyList[vertex]
        for (mstVertex, fringeVertex, weight) in adjacentEdges:

            if weight < distances[fringeVertex]:
                # update is necessary
                distances[fringeVertex] = weight
                parents[fringeVertex] = mstVertex
                vertexHeap.decreaseKey(fringeVertex, weight)

    mst = []
    for vertex in markedVertices:
        mst.append((parents[vertex], vertex, distances[vertex]))
    return mst


class IndexMinHeap(object):
    '''
    An indexed min-heap.

    Adapted from code by Robert Sedgewick and Kevin Wayne:
    http://www.cs.princeton.edu/algs4/43mst/IndexMinPQ.java.html
    '''
    def __init__(self, size):
        self.N = 0 # size of the heap to start off with
        self.pq = [None]*(size+1) # binary heap
        self.qp = [-1]*(size+1) # inverse of pq
        self.keys = [None]*(size+1) # keys[i] = priority of i

    # insert a key into the heap with a given value
    def insert(self, k, value):
        self.N += 1
        self.qp[k] = self.N
        self.pq[self.N] = k
        self.keys[k] = value
        self.swim(self.N)

    # get the key of the minimum
    def minKey(self):
        return self.keys[self.pq[1]]

    # pop off the minimum value, restore heap, and return minimum value
    def delMin(self):
        min = self.pq[1]
        self.exch( 1, self.N )
        self.N -= 1
        self.sink(1)
        self.qp[min] = -1
        return min

    # decrease the value of a key to a lower value
    def decreaseKey(self, k, newValue):
        self.keys[k] = newValue
        self.swim( self.qp[k] )

    # if the value at i > j, return true. otherwise, return false.
    def greater(self, i, j):
        return self.keys[self.pq[i]] > self.keys[self.pq[j]]

    # swap the locations of two nodes
    def exch(self, i, j):
        swap = self.pq[i]
        self.pq[i] = self.pq[j]
        self.pq[j] = swap
        self.qp[self.pq[i]] = i
        self.qp[self.pq[j]] = j

    # bubble a value up
    def swim(self, k):
        while k > 1 and self.greater(k/2, k):
            self.exch(k, k/2)
            k = k/2

    # sink a value down
    def sink(self, k):
        while (2*k) <= self.N:
            j = 2*k
            if j < self.N and self.greater(j, j+1):
                j += 1
            if not self.greater(k, j):
                break
            self.exch(k, j)
            k = j

# Test code
if __name__ == "__main__":
    # Create a list of vertices
    vertices = [0, 1, 2, 3, 4]
    # List of edges - each edge is in the form: (vertex1, vertex2, weight)
    edges = [(0, 1, 3.0), (0, 3, 5.0), (0, 2, 1.5), (1, 3, 3.0), (4, 0, 2.0)]
    print primMST(vertices, edges)
