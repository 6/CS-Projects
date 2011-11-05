def heapsort(array, n):
    '''
    Sorts an unsorted array of numbers (works with integers and floats).

    param array: an unsorted array
    param n: the number of elements in array
    return: sorted array
    ''' 

    def buildHeapFromArray():
        '''
        Convert initial array to binary max-heap form.
        '''
        # start at the last possible parent node
        startIdx = (n - 2) / 2
        
        # build heap from the bottom up
        while startIdx >= 0:
            restoreHeap(startIdx, n)
            startIdx -= 1

    def buildSortedArrayFromHeap():
        '''
        Build a sorted array from the binary max-heap.
        '''
        end = n - 1
        while end > 0:
            swapValues(end, 0)
            restoreHeap(0, end)
            end -= 1


    def restoreHeap(startIdx, endIdx):
        '''
        Restores heap properties in the array from startIdx to endIdx when a heap
        becomes invalid.
        '''
        parentIdx = startIdx

        while getLeftChildIdx(parentIdx) < endIdx:            

            leftChildIdx = getLeftChildIdx(parentIdx)
            rightChildIdx = leftChildIdx + 1

            # find the index of the larger child node
            largerChildIdx = leftChildIdx

            if rightChildIdx < endIdx:
                # right child exists
                
                if isSmaller(array[leftChildIdx], array[rightChildIdx]):  
                    largerChildIdx = rightChildIdx

            # parent should be >= child: swap if necessary
            if isSmaller(array[parentIdx], array[largerChildIdx]):
                swapValues(parentIdx, largerChildIdx)
                parentIdx = largerChildIdx

            else:
                # parent and it's children are in the correct order
                return


    def swapValues(indexA, indexB):
        '''
        Swaps the values of nodes at indexA and indexB in array. 
        '''
        array[indexA], array[indexB] = array[indexB], array[indexA]

    
    def isSmaller(value1, value2):
        '''
        Tests if value1 is smaller than value2.
        Returns true if value1 < value2. Returns false if value1 >= value2.
        '''
        return value1 < value2


    def getLeftChildIdx(parentIdx):
        '''
        Return index of the left child node of a parent given the parent index.
        '''
        return parentIdx * 2 + 1


    # Step 1: Convert array to binary max-heap form.
    buildHeapFromArray()

    # Step 2: Sort the array.
    buildSortedArrayFromHeap()
    
    return array


if __name__ == "__main__":
    # Test heapsort
    import random

    # generate 10 random integers
    randInts = random.sample(xrange(-100, 100), 10)

    print "Unsorted Array:", randInts
    randInts = heapsort(randInts, len(randInts))
    print "Sorted Array:", randInts
