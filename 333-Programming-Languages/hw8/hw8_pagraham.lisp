;; generate a random list of n numbers
(defun randomList (size maxNum)
  (if ( <= size 0)
    '()
    (cons (random maxNum) (randomList (- size 1) maxNum)) ))

;; splits a list into 2 halves returns a list of lists containing the left and
;; right halves. In cases where list length is odd, the left half will contain
;; one more item than the right half.
(defun split (data)
  (let ((mid (ceiling (/ (length data) 2))))
    (list (subseq data 0 mid) (subseq data mid))))

;; merge two sorted lists, maintaining the sorted ordering (<)
(defun smerge (left right)
  (cond
    ; check if either half is empty, and if so, return the other half
    ((eq (length left) 0) right)
    ((eq (length right) 0) left)
    ; if left[0] < right[0]
    ((< (first left) (first right)) (cons (first left) (smerge (rest left) right)))
    ; if left[0] >= right[0]
    ((>= (first left) (first right)) (cons (first right) (smerge left (rest right))))))

;; sort a list using the merge sort algorithm
(defun merge-sort (data)
    (if (<= (length data) 1) ; test base case
    	data ; then base case
    	(let ((sdata (split data)))	; else
        (smerge (merge-sort (car sdata)) (merge-sort (cadr sdata))))))

;; test split
(let ((data (randomList 11 100)))
  (print "Testing split")
  (print data)
  (print (split data)))

;; test smerge function
(let ((data1 '(1 2 4 5 6)))
  (let ((data2 '(2 3 5 5 8)))
    (print "Testing smerge")
    (print data1)
    (print data2)
    (print (smerge data1 data2))))

;; run mergesort on a random list of 10 numbers
(let ((data (randomList 10 100)))
  (print "Running merge-sort")
  (print data)
  (print (merge-sort data)))
