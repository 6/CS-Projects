# Recursive binary search algorithm based off pseudocode from:
# http://en.wikipedia.org/wiki/Binary_search_algorithm
bsearch = (list, value, low, high) ->
  if high < low
    return -1
  mid = low + Math.floor((high - low) / 2)
  if list[mid] > value
    return bsearch(list, value, low, mid-1)
  else if list[mid] < value
    return bsearch(list, value, mid+1, high)
  else
    return mid # found
    
list = [1,2,3,6,7,8,9,9]
console.log bsearch(list, 3, 0, list.length - 1)