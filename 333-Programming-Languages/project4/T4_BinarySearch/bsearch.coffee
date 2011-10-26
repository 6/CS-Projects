"""
Authors: Sarah Harmon, Peter Graham
Description: Project 4, Task 4
"""

# Recursive binary search algorithm based off pseudocode from:
# http://en.wikipedia.org/wiki/Binary_search_algorithm
bsearch = (list, value, low, high) ->
  return -1 if high < low
  mid = low + Math.floor (high - low) / 2
  return bsearch list, value, low, mid-1 if list[mid] > value
  return bsearch list, value, mid+1, high if list[mid] < value
  mid # found
    
list = [1,2,3,6,7,8,9,9]
console.log bsearch(list, 3, 0, list.length - 1)
